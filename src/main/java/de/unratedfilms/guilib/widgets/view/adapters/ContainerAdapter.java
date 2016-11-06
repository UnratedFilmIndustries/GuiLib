
package de.unratedfilms.guilib.widgets.view.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Point;
import de.unratedfilms.guilib.core.Rectangle;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetAdapter;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.core.WidgetRigid;
import de.unratedfilms.guilib.core.WidgetTooltipable;
import de.unratedfilms.guilib.widgets.model.Container;

/**
 * A minimal implementation of {@link Container}.
 */
public abstract class ContainerAdapter extends WidgetAdapter implements Container {

    private final List<LayoutManager>      layoutManagers     = new ArrayList<>();

    private final List<Widget>             widgets            = new ArrayList<>();
    private final List<WidgetFocusable>    focusableWidgets   = new ArrayList<>();

    // Keys & mouse
    private final Map<MouseButton, Widget> lastClickedWidgets = new HashMap<>();

    // Hover and tooltips
    private Widget                         hoveredWidget;
    private long                           hoverStart;                            // nanoseconds

    @Override
    public Container appendLayoutManager(LayoutManager layoutManager) {

        layoutManagers.add(layoutManager);
        return this;
    }

    @Override
    public List<Widget> getWidgets() {

        return widgets;
    }

    @Override
    public List<WidgetFocusable> getFocusableWidgets() {

        return focusableWidgets;
    }

    @Override
    public void addWidget(Widget widget) {

        widgets.add(widget);

        if (widget instanceof WidgetFocusable) {
            focusableWidgets.add((WidgetFocusable) widget);
        }
    }

    @Override
    public void addWidgets(Widget... widgets) {

        for (Widget widget : widgets) {
            addWidget(widget);
        }
    }

    @Override
    public boolean isFocused() {

        return getFocusedWidget() != null;
    }

    @Override
    public WidgetFocusable getFocusedWidget() {

        for (WidgetFocusable widget : focusableWidgets) {
            if (widget.isFocused()) {
                return widget;
            }
        }

        return null;
    }

    /*
     * Event handlers
     */

    @Override
    public boolean revalidate(boolean force) {

        boolean rev = !valid || force;

        // First, we revalidate all the rigid widgets which don't care about their position
        for (Widget widget : widgets) {
            if (widget instanceof WidgetRigid) {
                rev |= widget.revalidate(force);
            }
        }

        // Second, we revalidate the container itself; it can now arrange the widgets after resizing the flexible ones
        if (rev) {
            doRevalidate();
        }

        // Third, we revalidate the flexible widgets; they might have been resized during the second revalidation step
        for (Widget widget : widgets) {
            if (widget instanceof WidgetFlexible) {
                widget.revalidate(rev);
            }
        }

        valid = true;
        return rev;
    }

    @Override
    protected void doRevalidate() {

        for (LayoutManager layoutManager : layoutManagers) {
            layoutManager.layout();
        }
    }

    @Override
    public void update() {

        for (Widget widget : widgets) {
            widget.update();
        }
    }

    @Override
    public void draw(Viewport viewport, int mx, int my) {

        Viewport subViewport = subViewport(viewport);

        // Detect which widget the mouse cursor is hovering over
        detectHoveredWidget(subViewport, mx, my);

        WidgetFocusable focusedWidget = getFocusedWidget();

        // Draw all widgets apart from the focused one
        for (Widget widget : widgets) {
            if (widget != focusedWidget) {
                widget.draw(subViewport, mx, my);
            }
        }

        // If we have a focused widget, it is drawn last so that it can "overdraw" all the other widgets
        if (focusedWidget != null) {
            focusedWidget.draw(subViewport, mx, my);
        }

        // Draw the tooltip, if any
        if (hoveredWidget instanceof WidgetTooltipable) {
            int hoveredMillis = (int) ( (System.nanoTime() - hoverStart) / 1000 / 1000);
            Widget tooltip = ((WidgetTooltipable) hoveredWidget).getTooltip(hoveredMillis);

            tooltip.revalidate(false);
            tooltip.draw(getTooltipViewport(viewport, tooltip, mx, my), mx, my);
        }
    }

    private void detectHoveredWidget(Viewport viewport, int mx, int my) {

        for (Widget widget : widgets) {
            if (widget.inGlobalBounds(viewport, mx, my)) {
                // If w is being hovered over and it's not the current hovered widget, set it as that and start a new hovering clock
                if (widget != hoveredWidget) {
                    hoveredWidget = widget;
                    hoverStart = System.nanoTime();
                }

                // Make sure that we don't accidentally reset the hovered widget through the last instruction in this method!
                return;
            }
        }

        hoveredWidget = null;
    }

    private Viewport getTooltipViewport(Viewport containerViewport, Widget tooltip, int mx, int my) {

        // Move the tooltip widget to the mouse cursor
        Point offset = new Point(mx + 12, my - 12);

        // Shift the tooltip widget if it would clip out of the right border of the window
        if (offset.getX() + tooltip.getWidth() + 6 > MC.displayWidth) {
            offset = offset.withX(offset.getX() - 28 - tooltip.getWidth());
        }

        // Shift the tooltip widget if it would clip out of the lower border of the window
        if (offset.getY() + tooltip.getHeight() + 6 > MC.displayHeight) {
            offset = offset.withY(MC.displayHeight - tooltip.getHeight() - 6);
        }

        // Create a new viewport without any effective scissor area
        return containerViewport.withWidgetOffset(offset).withoutScissor();
    }

    @Override
    public void focusGained() {

        // Empty
    }

    @Override
    public void focusLost() {

        // Empty
    }

    @Override
    public boolean mousePressed(Viewport viewport, int mx, int my, MouseButton mouseButton) {

        Viewport subViewport = subViewport(viewport);

        // If we have a focused widget, it is allowed to handle the mouse click first
        WidgetFocusable focusedWidget = getFocusedWidget();
        if (focusedWidget != null && tryForwardMousePressedToWidget(subViewport, mx, my, mouseButton, focusedWidget)) {
            return true;
        }

        // If we don't have a focused widget or the focused widget is not interested in the mouse click, allow the other widgets to handle it
        Widget clickedWidget = null;
        for (Widget widget : widgets) {
            if (widget != focusedWidget /* the focused widget already had its chance */ && tryForwardMousePressedToWidget(subViewport, mx, my, mouseButton, widget)) {
                clickedWidget = widget;
                break;
            }
        }

        // If the player clicked a focusable widget, focus that widget, and further make sure that the previously focused widget isn't focused anymore
        WidgetFocusable previouslyFocusedWidget = getFocusedWidget();
        if (clickedWidget != previouslyFocusedWidget) {
            if (previouslyFocusedWidget != null) {
                previouslyFocusedWidget.focusLost();
            }
            if (clickedWidget instanceof WidgetFocusable) {
                ((WidgetFocusable) clickedWidget).focusGained();
            }
        }

        return clickedWidget != null;
    }

    private boolean tryForwardMousePressedToWidget(Viewport subViewport, int mx, int my, MouseButton mouseButton, Widget widget) {

        if (widget.mousePressed(subViewport, mx, my, mouseButton)) {
            lastClickedWidgets.put(mouseButton, widget);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void mouseReleased(Viewport viewport, int mx, int my, MouseButton mouseButton) {

        if (lastClickedWidgets.containsKey(mouseButton)) {
            lastClickedWidgets.get(mouseButton).mouseReleased(subViewport(viewport), mx, my, mouseButton);
            lastClickedWidgets.remove(mouseButton);
        }
    }

    @Override
    public boolean mouseWheel(Viewport viewport, int mx, int my, int delta) {

        Viewport subViewport = subViewport(viewport);

        // If we have a focused widget, it is allowed to handle the mouse wheel event first
        WidgetFocusable focusedWidget = getFocusedWidget();
        if (focusedWidget != null && focusedWidget.mouseWheel(subViewport, mx, my, delta)) {
            return true;
        }

        // If we don't have a focused widget or the focused widget is not interested in the key event, allow the other widgets to handle it
        for (Widget widget : widgets) {
            if (widget != focusedWidget /* the focused widget already had its chance */ && widget.mouseWheel(subViewport, mx, my, delta)) {
                return true;
            }
        }

        // Apparently, nobody's interested
        return false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {

        // If we have a focused widget, it is allowed to handle the key event first
        WidgetFocusable focusedWidget = getFocusedWidget();
        if (focusedWidget != null && focusedWidget.keyTyped(typedChar, keyCode)) {
            return true;
        }

        // If we don't have a focused widget or the focused widget is not interested in the key event, allow the other widgets to handle it
        for (Widget widget : widgets) {
            if (widget != focusedWidget /* the focused widget already had its chance */ && widget.keyTyped(typedChar, keyCode)) {
                return true;
            }
        }

        // If even that had no effect and no widget wanted to handle the event, try using it for internal purposes
        switch (keyCode) {
            case Keyboard.KEY_TAB:
                shiftFocusToNext();
                return true;
        }

        // Okay, that key seems to be really lame; we apparently don't care about it being pressed
        return false;
    }

    protected Viewport subViewport(Viewport parent) {

        Point globalPosition = parent.globalPosition(getPosition());

        // Offset all widget's inside this container by the position of this container
        Viewport sub = parent.withWidgetOffset(globalPosition);

        // Make sure that the new sub-viewport does not poke out of this viewport
        sub = sub.withScissor(parent.getScissor().intersection(new Rectangle(globalPosition, getWidth(), getHeight())));

        return sub;
    }

    protected void shiftFocusToNext() {

        if (isFocused() /* has a widget that is in focus */) {
            WidgetFocusable currentlyFocusedWidget = getFocusedWidget();
            int currentFocusIndex = focusableWidgets.indexOf(getFocusedWidget());
            int newFocusIndex = (currentFocusIndex + 1) % focusableWidgets.size();

            if (currentFocusIndex != newFocusIndex) {
                currentlyFocusedWidget.focusLost();
                focusableWidgets.get(newFocusIndex).focusGained();
            }
        }
    }

}

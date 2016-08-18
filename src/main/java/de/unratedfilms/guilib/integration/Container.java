
package de.unratedfilms.guilib.integration;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import de.unratedfilms.guilib.core.FocusableWidget;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.TooltipableWidget;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.Scrollbar;

public class Container {

    // Utility constant for quick access
    private static final Minecraft      MC                 = Minecraft.getMinecraft();

    private final List<Widget>          widgets            = new ArrayList<>();
    private final List<FocusableWidget> focusableWidgets   = new ArrayList<>();

    private int                         left, right, top, bottom, contentHeight;
    private boolean                     clipping           = true;

    // Scrolling
    private final int                   shiftAmount, extraScrollHeight;
    private int                         scrollbarWidth;
    private final Scrollbar             scrollbar;

    // Focus
    private Widget                      lastSelectedWidget;
    private int                         focusedWidgetIndex = -1;

    // Hover and tooltips
    private Widget                      hoveredWidget;
    private long                        hoverStart;                                   // nanoseconds

    /**
     * Creates a new container, which by default will clip the elements contained.
     */
    public Container() {

        this(null, 0, 0);
    }

    /**
     * Creates a new container with a {@link Scrollbar}, which by default will clip the elements contained.
     *
     * @param scrollbar The scrollbar for this container.
     * @param shiftAmount The amount to shift the scrollbar when focus is shifted;
     *        this should normally be the height of the FocusableWidget, depending on spacing.
     * @param extraScrollHeight The sum of the supposed gap between the top+bottom of this container
     *        and the FocusableWidgets that are inside. Say that the items in the list should go from this container's top+2
     *        to this container's bottom-2, then extraScrollHeight should be 4.
     */
    public Container(Scrollbar scrollbar, int shiftAmount, int extraScrollHeight) {

        this.scrollbar = scrollbar;
        this.shiftAmount = shiftAmount;
        this.extraScrollHeight = extraScrollHeight;

        if (scrollbar != null) {
            scrollbar.setContainer(this);
            scrollbarWidth = scrollbar.getWidth();
        }
    }

    public List<Widget> getWidgets() {

        return widgets;
    }

    public List<FocusableWidget> getFocusableWidgets() {

        return focusableWidgets;
    }

    public void addWidgets(Widget... widgets) {

        for (Widget widget : widgets) {
            this.widgets.add(widget);
            if (widget instanceof FocusableWidget) {
                focusableWidgets.add((FocusableWidget) widget);
            }
        }

        calculateContentHeight();
    }

    public int left() {

        return left;
    }

    public int right() {

        return right;
    }

    public int top() {

        return top;
    }

    public int bottom() {

        return bottom;
    }

    public int getContentHeight() {

        return contentHeight;
    }

    public void setClipping(boolean clipping) {

        this.clipping = clipping;
    }

    public boolean isClipping() {

        return clipping;
    }

    public void revalidate(int x, int y, int width, int height) {

        left = x;
        right = x + width;
        top = y;
        bottom = y + height;
        calculateContentHeight();
        if (scrollbar != null) {
            scrollbar.revalidate(top, bottom);
            scrollbarWidth = scrollbar.getWidth();
        }
    }

    private void calculateContentHeight() {

        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (Widget w : widgets) {
            if (w.getY() < minY) {
                minY = w.getY();
            }
            if (w.getY() + w.getHeight() > maxY) {
                maxY = w.getY() + w.getHeight();
            }
        }
        contentHeight = minY > maxY ? 0 : maxY - minY + extraScrollHeight;
    }

    public void update() {

        for (Widget w : widgets) {
            w.update();
        }
    }

    // mx, my are mouse coordinates
    public void draw(int mx, int my, int scale) {

        /*
         * If the mouse cursor is outside this container and we clip, this fix ensures that nobody thinks the mouse cursor is above a widget.
         * Sadly, this dirty fix is necessary since there's no other way to tell the widgets that they are partially clipped.
         */
        boolean mouseInBounds = inBounds(mx, my);
        int mxClipped = mouseInBounds || !clipping ? mx : -1;
        int myClipped = mouseInBounds || !clipping ? my : -1;

        // Detect which widget the mouse cursor is hovering over
        detectHoveredWidget(mxClipped, myClipped);

        if (clipping) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(left * scale, MC.displayHeight - bottom * scale, (right - left - scrollbarWidth) * scale, (bottom - top) * scale);
        }

        // Draw all widgets and get the tooltip if there should be one
        Widget tooltip = null;
        for (Widget w : widgets) {
            if (w.shouldRender(top, bottom)) {
                w.draw(mxClipped, myClipped);

                if (w instanceof TooltipableWidget && w == hoveredWidget) {
                    int hoveredMillis = (int) ( (System.nanoTime() - hoverStart) / 1000 / 1000);
                    tooltip = ((TooltipableWidget) w).getTooltip(hoveredMillis);
                    adjustTooltipBounds(tooltip, mx, my);
                }
            }
        }

        // Don't clip the scrollbar and the tooltip!
        if (clipping) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
            scrollbar.draw(mx, my);
        }

        // Draw the tooltip, if any
        if (tooltip != null) {
            tooltip.draw(mx, my);
        }
    }

    private void detectHoveredWidget(int mx, int my) {

        for (Widget w : widgets) {
            if (w.inBounds(mx, my)) {
                // If w is being hovered over and it's not the current hovered widget, set it as that and start a new hovering clock
                if (w != hoveredWidget) {
                    hoveredWidget = w;
                    hoverStart = System.nanoTime();
                }

                // Make sure that we don't accidentally reset the hovered widget through the last instruction in this method!
                return;
            }
        }

        hoveredWidget = null;
    }

    private void adjustTooltipBounds(Widget tooltip, int mx, int my) {

        // Move the tooltip widget to the mouse cursor
        tooltip.setPosition(mx + 12, my - 12);

        // Shift the tooltip widget if it would clip out of the right border of the window
        if (tooltip.getX() + tooltip.getWidth() + 6 > MC.currentScreen.width) {
            tooltip.setX(tooltip.getX() - 28 - tooltip.getWidth());
        }

        // Shift the tooltip widget if it would clip out of the lower border of the window
        if (tooltip.getY() + tooltip.getHeight() + 6 > MC.currentScreen.height) {
            tooltip.setY(MC.displayHeight - tooltip.getHeight() - 6);
        }
    }

    public void setFocused(FocusableWidget f) {

        int newIndex = f == null ? -1 : focusableWidgets.indexOf(f);
        if (focusedWidgetIndex != newIndex) {
            if (focusedWidgetIndex != -1) {
                focusableWidgets.get(focusedWidgetIndex).focusLost();
            }
            if (newIndex != -1) {
                focusableWidgets.get(newIndex).focusGained();
            }

            focusedWidgetIndex = newIndex;
        }
    }

    public FocusableWidget deleteFocused() {

        if (hasFocusedWidget()) {
            FocusableWidget w = getFocusedWidget();
            if (lastSelectedWidget == w) {
                lastSelectedWidget = null;
            }
            focusableWidgets.remove(focusedWidgetIndex);
            if (focusableWidgets.size() == 0) {
                focusedWidgetIndex = -1;
            } else {
                focusedWidgetIndex = MathHelper.clamp_int(focusedWidgetIndex, 0, focusableWidgets.size() - 1);
                focusableWidgets.get(focusedWidgetIndex).focusGained();
            }

            int index = widgets.indexOf(w), offset = Integer.MAX_VALUE;
            for (int i = index + 1; i < widgets.size(); ++i) {
                Widget cur = widgets.get(i);
                if (offset == Integer.MAX_VALUE) {
                    offset = w.getY() - cur.getY();
                }
                cur.setY(cur.getY() + offset);
            }
            widgets.remove(w);
            calculateContentHeight();
            if (scrollbar != null) {
                scrollbar.onChildRemoved();
            }

            return w;
        }
        return null;
    }

    public void removeFocusableWidgets() {

        focusedWidgetIndex = -1;
        if (lastSelectedWidget instanceof FocusableWidget) {
            lastSelectedWidget = null;
        }

        widgets.removeAll(focusableWidgets);
        focusableWidgets.clear();

        calculateContentHeight();
        if (scrollbar != null) {
            scrollbar.onChildRemoved();
        }
    }

    public boolean hasFocusedWidget() {

        return focusedWidgetIndex != -1;
    }

    public FocusableWidget getFocusedWidget() {

        return hasFocusedWidget() ? focusableWidgets.get(focusedWidgetIndex) : null;
    }

    protected void shiftFocusToNext() {

        if (focusedWidgetIndex != -1 && focusableWidgets.size() > 1) {
            int newIndex = (focusedWidgetIndex + 1) % focusableWidgets.size();
            if (newIndex != focusedWidgetIndex) {
                focusableWidgets.get(focusedWidgetIndex).focusLost();
                focusableWidgets.get(newIndex).focusGained();
                if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
                    scrollbar.shift( (focusedWidgetIndex - newIndex) * shiftAmount);
                }
                focusedWidgetIndex = newIndex;
            }
        }
    }

    protected void shiftFocus(int newIndex) {

        if (focusedWidgetIndex != newIndex) {
            focusableWidgets.get(focusedWidgetIndex).focusLost();
            focusableWidgets.get(newIndex).focusGained();
            if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
                scrollbar.shift( (focusedWidgetIndex - newIndex) * shiftAmount);
            }
            focusedWidgetIndex = newIndex;
        }
    }

    protected void shift(int delta) {

        if (focusedWidgetIndex != -1) {
            shiftFocus(MathHelper.clamp_int(focusedWidgetIndex + delta, 0, focusableWidgets.size() - 1));
        } else if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
            scrollbar.shiftRelative(delta * -4);
        }
    }

    public boolean inBounds(int mx, int my) {

        return mx >= left && my >= top && mx < right && my < bottom;
    }

    public boolean mousePressed(int mx, int my, MouseButton mouseButton) {

        if (inBounds(mx, my)) {
            boolean resetFocus = true;

            if (scrollbar != null && scrollbar.shouldRender(top, bottom) && scrollbar.inBounds(mx, my)) {
                return true;
            }

            for (Widget w : widgets) {
                if (w.shouldRender(top, bottom) && w.mousePressed(mx, my, mouseButton)) {
                    lastSelectedWidget = w;
                    if (w instanceof FocusableWidget) {
                        setFocused((FocusableWidget) w);
                        resetFocus = false;
                    }
                    break;
                }
            }
            if (resetFocus) {
                setFocused(null);
            }
            return true;
        }
        return false;
    }

    public void mouseReleased(int mx, int my, MouseButton mouseButton) {

        if (lastSelectedWidget != null) {
            lastSelectedWidget.mouseReleased(mx, my, mouseButton);
            lastSelectedWidget = null;
        }
    }

    public void mouseWheel(int delta) {

        if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
            scrollbar.shiftRelative(delta);
        } else {
            if (focusedWidgetIndex != -1) {
                focusableWidgets.get(focusedWidgetIndex).mouseWheel(delta);
            } else {
                for (Widget widget : widgets) {
                    if (widget.mouseWheel(delta)) {
                        break;
                    }
                }
            }
        }
    }

    public boolean keyTyped(char typedChar, int keyCode) {

        boolean handled = focusedWidgetIndex != -1 ? focusableWidgets.get(focusedWidgetIndex).keyTyped(typedChar, keyCode) : false;
        if (!handled) {
            switch (keyCode) {
                case Keyboard.KEY_UP:
                    shift(-1);
                    handled = true;
                    break;
                case Keyboard.KEY_DOWN:
                    shift(1);
                    handled = true;
                    break;
                case Keyboard.KEY_TAB:
                    shiftFocusToNext();
                    handled = true;
                    break;
            }
        }
        return handled;
    }

}

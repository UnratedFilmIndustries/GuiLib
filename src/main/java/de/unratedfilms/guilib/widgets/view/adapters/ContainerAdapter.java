
package de.unratedfilms.guilib.widgets.view.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.unratedfilms.guilib.core.Point;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetAdapter;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.core.WidgetParent;
import de.unratedfilms.guilib.core.WidgetTooltipable;
import de.unratedfilms.guilib.util.Utils;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible.ViewportAwareLayoutManager;
import de.unratedfilms.guilib.widgets.model.ContainerRigid;

/**
 * An minimal implementation of {@link Container}.
 * It both supports {@link ContainerRigid} and {@link ContainerFlexible}.
 * Just don't forget to implement either of the two interfaces after extending this generic adapter!
 */
public abstract class ContainerAdapter extends WidgetAdapter implements Container, WidgetParent {

    private final List<LayoutManager>              layoutManagers              = new ArrayList<>();
    private final List<ViewportAwareLayoutManager> viewportAwareLayoutManagers = new ArrayList<>();

    private ImmutableList<Widget>                  widgets                     = ImmutableList.of();
    private ImmutableList<WidgetFocusable>         focusableWidgets            = ImmutableList.of();

    // Hover and tooltips
    private Widget                                 hoveredWidget;
    private long                                   hoverStart;                                      // nanoseconds
    private Widget                                 tooltip;

    public ContainerAdapter(Widget... widgets) {

        addWidgets(widgets);
    }

    @Override
    public ContainerAdapter appendLayoutManager(LayoutManager layoutManager) {

        layoutManagers.add(layoutManager);
        return this;
    }

    /*
     * Only used in flexible containers!
     * When in a non-flexible-container, this method is still there but cannot be accessed, as only the ContainerFlexible interface declares this method.
     */
    public ContainerFlexible appendLayoutManager(ViewportAwareLayoutManager layoutManager) {

        viewportAwareLayoutManagers.add(layoutManager);
        return (ContainerFlexible) this;
    }

    @Override
    public ImmutableList<Widget> getWidgets() {

        return widgets;
    }

    @Override
    public ImmutableList<WidgetFocusable> getFocusableWidgets() {

        return focusableWidgets;
    }

    @Override
    public ContainerAdapter addWidgets(Iterable<Widget> widgets) {

        this.widgets = ImmutableList.<Widget> builder().addAll(this.widgets).addAll(widgets).build();

        Iterator<WidgetFocusable> newFocusableWidgets = StreamSupport.stream(widgets.spliterator(), false)
                .filter(w -> w instanceof WidgetFocusable).map(WidgetFocusable.class::cast).iterator();
        focusableWidgets = ImmutableList.<WidgetFocusable> builder().addAll(focusableWidgets).addAll(newFocusableWidgets).build();

        invalidate();

        return this;
    }

    @Override
    public ContainerAdapter removeWidgets(Iterable<Widget> widgets) {

        this.widgets = ImmutableList.copyOf(this.widgets.stream().filter(w -> !Iterables.contains(widgets, w)).iterator());
        focusableWidgets = ImmutableList.copyOf(focusableWidgets.stream().filter(w -> !Iterables.contains(widgets, w)).iterator());

        invalidate();

        return this;
    }

    @Override
    public ContainerAdapter clearWidgets() {

        widgets = ImmutableList.of();
        focusableWidgets = ImmutableList.of();

        invalidate();

        return this;
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

    @Override
    public List<Widget> getChildren() {

        // Also consider the tooltip, if any
        return tooltip == null ? widgets : Utils.mutableListWith(widgets, tooltip);
    }

    /*
     * Event handlers
     */

    @Override
    protected void revalidateThis() {

        for (LayoutManager layoutManager : layoutManagers) {
            layoutManager.layout(this);
        }
    }

    /*
     * Only used in flexible containers!
     * When in a non-flexible-container, this method is still there but never called.
     */
    @Override
    protected void revalidateThis(Viewport viewport) {

        for (ViewportAwareLayoutManager layoutManager : viewportAwareLayoutManagers) {
            layoutManager.layout((ContainerFlexible) this, viewport);
        }
    }

    @Override
    public void draw(Viewport viewport, int mx, int my) {

        // Detect which widget the mouse cursor is hovering over
        detectHoveredWidget(viewport, mx, my);

        // If the hovered widget can supply a tooltip, store it in the "tooltip" member variable for getChildren() to use; otherwise, set "tooltip" to null
        // TODO: Replace this hacked-together tooltip rendering mechanism with a proper mechanism that's executed by the recursions themselves or placed in the widgets otherwise
        if (hoveredWidget instanceof WidgetTooltipable) {
            int hoveredMillis = (int) ( (System.nanoTime() - hoverStart) / 1000 / 1000);
            tooltip = ((WidgetTooltipable) hoveredWidget).getTooltip(hoveredMillis);
        } else {
            tooltip = null;
        }
    }

    private void detectHoveredWidget(Viewport viewport, int mx, int my) {

        for (Widget widget : widgets) {
            if (widget.inGlobalBounds(getChildViewport(viewport, widget), mx, my)) {
                // If the widget is being hovered over and it's not the current hovered widget, set it as that and start a new hovering clock
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

    @Override
    public Viewport getChildViewport(Viewport viewport, Widget child) {

        if (child == tooltip) {
            // A quick and dirty hack to get the location of the mouse at this point in time
            int mx = viewport.getScreenSize().getWidth() * Mouse.getX() / MC.displayWidth;
            int my = viewport.getScreenSize().getHeight() * (1 - Mouse.getY() / MC.displayHeight) - 1;

            return getTooltipViewport(viewport, child, mx, my);
        }
        // All regular widgets that this container contains
        else {
            return WidgetParent.super.getChildViewport(viewport, child);
        }
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
    public boolean keyTyped(char typedChar, int keyCode) {

        // If no widget wanted to handle the key press, try using it for internal purposes
        switch (keyCode) {
            case Keyboard.KEY_TAB:
                shiftFocusToNext();
                return true;
        }

        // Okay, that key seems to be really lame; we apparently don't care about it being pressed
        return false;
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

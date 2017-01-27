
package de.unratedfilms.guilib.widgets.view.impl;

import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Keyboard;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Point;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Scrollbar;

/**
 * A {@link Container} that can display a scrollbar so that the player can scroll to the elements which are out of the container's bounds at first.
 */
public class ContainerScrollableImpl extends ContainerClippingImpl {

    private final Scrollbar scrollbar;
    private final int       shiftAmount;

    /**
     * Creates a new scrollable container, which by default will clip the elements contained.
     *
     * @param scrollbar The scrollbar for this container.
     * @param shiftAmount The amount to shift the scrollbar when focus is shifted.
     *        This should normally be the height of the {@link WidgetFocusable}, depending on spacing.
     * @throws IllegalArgumentException If either the given width or the given height is negative.
     */
    public ContainerScrollableImpl(Scrollbar scrollbar, int shiftAmount) {

        Validate.notNull(scrollbar, "Scrollbar cannot be null in a scrollable container");
        Validate.isTrue(scrollbar.getContainer() == null, "One scrollbar cannot be used in multiple scrollable containers");

        this.scrollbar = scrollbar;
        scrollbar.setContainer(this);

        this.shiftAmount = shiftAmount;
    }

    /**
     * Creates a new scrollable container, which by default will clip the elements contained, and immediately adds the given widgets to it.
     *
     * @param scrollbar The scrollbar for this container.
     * @param shiftAmount The amount to shift the scrollbar when focus is shifted.
     *        This should normally be the height of the {@link WidgetFocusable}, depending on spacing.
     * @param widgets The widgets that should initially be added to the new scrollable container.
     * @throws IllegalArgumentException If either the given width or the given height is negative.
     */
    public ContainerScrollableImpl(Scrollbar scrollbar, int shiftAmount, Widget... widgets) {

        super(widgets);

        Validate.notNull(scrollbar, "Scrollbar cannot be null in a scrollable container");
        Validate.isTrue(scrollbar.getContainer() == null, "One scrollbar cannot be used in multiple scrollable containers");

        this.scrollbar = scrollbar;
        scrollbar.setContainer(this);

        this.shiftAmount = shiftAmount;
    }

    /*
     * Event handlers
     */

    @Override
    public void update() {

        scrollbar.update();
        super.update();
    }

    @Override
    public boolean revalidate(boolean force) {

        // Revalidate the scrollbar; note that it is a rigid widget
        boolean scrollbarRevalidated = scrollbar.revalidate(!valid || force);

        // Revalidate the rest of the container
        return super.revalidate(force || scrollbarRevalidated);
    }

    /*
     * This method is used for the widget's inside the container, which need to be shifted by the scroll amount.
     * Note that the scrollbar itself still uses the super.subViewport() since it is not affected by the scrolling and should just sit next to the scrolled content!
     */
    @Override
    protected Viewport subViewport(Viewport parent) {

        Viewport sub = super.subViewport(parent);

        Point widgetOffset = sub.getWidgetOffset();
        // Note that we shift UP (-), not DOWN (+)!
        widgetOffset = widgetOffset.withY(widgetOffset.getY() - scrollbar.getWidgetShift());

        return sub.withWidgetOffset(widgetOffset);
    }

    @Override
    public void draw(Viewport viewport, int mx, int my) {

        super.draw(viewport, mx, my);

        scrollbar.draw(super.subViewport(viewport), mx, my);
    }

    @Override
    public boolean mousePressed(Viewport viewport, int mx, int my, MouseButton mouseButton) {

        if (scrollbar.inGlobalBounds(super.subViewport(viewport), mx, my)) {
            return true;
        } else {
            return super.mousePressed(viewport, mx, my, mouseButton);
        }
    }

    @Override
    public boolean mouseWheel(Viewport viewport, int mx, int my, int delta) {

        boolean widgetCapturedMouseWheel = super.mouseWheel(viewport, mx, my, delta);

        if (widgetCapturedMouseWheel) {
            return true;
        }
        // We catch all mouse wheel actions that take place over the area of this container and are not captured by any widget that's part of the container
        else if (!widgetCapturedMouseWheel && inGlobalBounds(viewport, mx, my)) {
            scrollbar.addWidgetShiftRelative(-delta);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {

        super.keyTyped(typedChar, keyCode);

        // If even that had no effect and no widget wanted to handle the event, try using it for internal purposes
        switch (keyCode) {
            case Keyboard.KEY_UP:
                scrollbar.addWidgetShiftRelative(-4);
                return true;
            case Keyboard.KEY_DOWN:
                scrollbar.addWidgetShiftRelative(4);
                return true;
        }

        // Okay, that key seems really lame; we apparently don't care about it being pressed
        return false;
    }

    @Override
    protected void shiftFocusToNext() {

        if (isFocused() /* has a widget that is in focus */) {
            int oldFocusIndex = getFocusableWidgets().indexOf(getFocusedWidget());
            super.shiftFocusToNext();
            int newFocusIndex = getFocusableWidgets().indexOf(getFocusedWidget());

            scrollbar.addWidgetShift( (newFocusIndex - oldFocusIndex) * shiftAmount);
        }
    }

}

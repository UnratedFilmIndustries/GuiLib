
package de.unratedfilms.guilib.widgets.view.impl;

import java.util.List;
import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Keyboard;
import de.unratedfilms.guilib.core.Point;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.util.Utils;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Scrollbar;

/**
 * A {@link Container} that can display a scrollbar so that the player can scroll to the elements which are out of the container's bounds at first.
 */
public class ContainerScrollableImpl extends ContainerClippingImpl {

    private final Scrollbar scrollbar;

    /**
     * Creates a new scrollable container, which by default will clip the elements contained.
     *
     * @param scrollbar The scrollbar for this container.
     * @throws IllegalArgumentException If either the given width or the given height is negative.
     */
    public ContainerScrollableImpl(Scrollbar scrollbar) {

        Validate.notNull(scrollbar, "Scrollbar cannot be null in a scrollable container");
        Validate.isTrue(scrollbar.getContainer() == null, "One scrollbar cannot be used in multiple scrollable containers");

        this.scrollbar = scrollbar;
        scrollbar.setContainer(this);
    }

    /**
     * Creates a new scrollable container, which by default will clip the elements contained, and immediately adds the given widgets to it.
     *
     * @param scrollbar The scrollbar for this container.
     * @param widgets The widgets that should initially be added to the new scrollable container.
     * @throws IllegalArgumentException If either the given width or the given height is negative.
     */
    public ContainerScrollableImpl(Scrollbar scrollbar, Widget... widgets) {

        super(widgets);

        Validate.notNull(scrollbar, "Scrollbar cannot be null in a scrollable container");
        Validate.isTrue(scrollbar.getContainer() == null, "One scrollbar cannot be used in multiple scrollable containers");

        this.scrollbar = scrollbar;
        scrollbar.setContainer(this);
    }

    /*
     * Event handlers
     */

    @Override
    public List<Widget> getChildren() {

        return Utils.mutableListWith(super.getChildren(), scrollbar);
    }

    /*
     * This method is used for the widget's inside the container, which need to be shifted by the scroll amount.
     * Note that the scrollbar itself still uses the super.getChildViewport() since it is not affected by the scrolling and should just sit next to the scrolled content!
     */
    @Override
    public Viewport getChildViewport(Viewport viewport, Widget child) {

        Viewport sub = super.getChildViewport(viewport, child);

        // The scrollbar should not scroll, neither shall the tooltip
        if (child == scrollbar || child == tooltip) {
            return sub;
        }
        // The user has added the child widget to the container; it should therefore scroll
        else {
            Point widgetOffset = sub.getWidgetOffset();
            // Note that we shift UP (-), not DOWN (+)!
            widgetOffset = widgetOffset.withY(widgetOffset.getY() - scrollbar.getWidgetShift());

            return sub.withWidgetOffset(widgetOffset);
        }
    }

    @Override
    public boolean mouseWheel(Viewport viewport, int mx, int my, int delta) {

        if (super.mouseWheel(viewport, mx, my, delta)) {
            return true;
        }

        // We catch all mouse wheel actions that take place over the area of this container and are not captured by the superclass (or any widget that's part of the container)
        if (inGlobalBounds(viewport, mx, my)) {
            scrollbar.addWidgetShiftRelative(-delta);
            return true;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {

        if (super.keyTyped(typedChar, keyCode)) {
            return true;
        }

        // If the superclass didn't want to handle the key press (and no widget as well), try using it for internal purposes
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

}

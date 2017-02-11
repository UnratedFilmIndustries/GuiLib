
package de.unratedfilms.guilib.core;

import java.util.List;

/**
 * A {@link Widget} that can hold a number of {@link #getChildren() child widgets}.
 * This kind of widget is typically known as a container, and in fact, the container widget in this library implements this interface.<br>
 * <br>
 * <b>The implementing widget <i>shall not</i> perform operations like revalidation or drawing recursively on the child widgets.</b>
 * That is done by an external function which accesses the child widgets of this parent widget through the {@link #getChildren()} method.
 */
public interface WidgetParent extends Widget, WidgetFocusAware {

    /**
     * Returns the child widgets which are held by this widget.
     * All these widgets will become part of the GUI automatically, meaning that they'll be revalidated, drawn, notified about mouse and keyboard input and so on.
     *
     * @return The child widgets of this parent widget.
     */
    public List<Widget> getChildren();

    default public WidgetFocusAware getFocusedChild() {

        for (Widget child : getChildren()) {
            if (child instanceof WidgetFocusAware && ((WidgetFocusAware) child).isFocused()) {
                return (WidgetFocusAware) child;
            }
        }

        return null;
    }

    @Override
    default boolean isFocused() {

        return getFocusedChild() != null;
    }

    /**
     * Given the {@link Viewport} that defines the location and rendering of this parent widget, calculates the viewport that should be used for the given {@link #getChildren() child widget}.
     * This method is heavily used by the recursions, since when descending down the widget tree, it creates the viewports that should be used for each child widget.<br>
     * <br>
     * Let's look at a simple example.
     * A regular clipping container would just add its own coordinates to the widget offset, thus making the child's coordinates relative to its position.
     * It would also adjust the scissor area to its bounds, resulting in child widgets being (partially) clipped in case they don't lie in the area of the container widget.
     * Note that in fact, that very behavior is the default one implemented by this method.
     *
     * @param viewport The basis viewport this parent widget itself is confronted with.
     * @param child The child widget whose sub-viewport should be calculated.
     *        This widget should of course be part of {@link #getChildren()}.
     * @return The sub-viewport for the given child widget.
     * @throws UnknownChildException If the child widget you have passed in is not actually a child of this parent widget.
     *         This exception is not guaranteed to be thrown, so be careful and don't rely on it!
     *         Passing in unknown child widgets might lead to undefined behavior.
     */
    default public Viewport getChildViewport(Viewport viewport, Widget child) {

        Point globalPosition = viewport.globalPosition(getPosition());

        // Offset all child widgets by the position of this parent widget
        Viewport sub = viewport.withWidgetOffset(globalPosition);

        // Make sure that the new sub-viewport does not poke out of this viewport
        sub = sub.withScissor(viewport.getScissor().intersection(new Rectangle(globalPosition, getSize())));

        return sub;
    }

    @SuppressWarnings ("serial")
    public static class UnknownChildException extends RuntimeException {

        private final WidgetParent parent;
        private final Widget       unknownChild;

        public UnknownChildException(WidgetParent parent, Widget unknownChild) {

            super("The parent widget '" + parent + "' doesn't have '" + unknownChild + "' as a child widget");

            this.parent = parent;
            this.unknownChild = unknownChild;
        }

        public WidgetParent getParent() {

            return parent;
        }

        public Widget getUnknownChild() {

            return unknownChild;
        }

    }

}

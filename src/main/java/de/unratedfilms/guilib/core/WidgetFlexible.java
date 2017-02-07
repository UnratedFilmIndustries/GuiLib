
package de.unratedfilms.guilib.core;

/**
 * A {@link Widget} that is flexible and therefore can be resized by outside callers.
 * The widget can provide a default size when it is created, but its {@link #revalidate(Viewport, boolean)} method is not allowed to change the widget's size afterwards.<br>
 * <br>
 * For example, widgets like labeled buttons or text fields are flexible since they can handle different sizes and are not restricted to just one size.
 */
public interface WidgetFlexible extends Widget {

    /**
     * Sets the width of this widget.
     *
     * @param width The new width in pixels.
     * @throws IllegalArgumentException If the given width is negative.
     */
    default public void setWidth(int width) {

        setExtent(Axis.X, width);
    }

    /**
     * Sets the height of this widget.
     *
     * @param height The new height in pixels.
     * @throws IllegalArgumentException If the given height is negative.
     */
    default public void setHeight(int height) {

        setExtent(Axis.Y, height);
    }

    /**
     * Sets the width or height of this widget, depending on whether the given {@link Axis} argument points to {@link Axis#X} or {@link Axis#Y}.
     *
     * @param axis Whether the {@link Axis#X} (width) or {@link Axis#Y} (height) extent should be changed.
     * @param extent The new width or height in pixels.
     * @throws IllegalArgumentException If the given width or height is negative.
     */
    public void setExtent(Axis axis, int extent);

    /**
     * Sets both the width and height of this widget at the same time.
     * Note that this is just a convenience method and internally calls {@link #setWidth(int)} and {@link #setHeight(int)}.
     *
     * @param width The new width in pixels.
     * @param height The new height in pixels.
     * @throws IllegalArgumentException If either the given width or the given height is negative.
     */
    default public void setSize(int width, int height) {

        setWidth(width);
        setHeight(height);
    }

    /**
     * Sets the width and height of this widget to the given size.
     * Note that this is just a convenience method and internally calls {@link #setWidth(int)} and {@link #setHeight(int)}.
     *
     * @param size The new size of the widget.
     */
    default public void setSize(Dimension size) {

        setSize(size.getWidth(), size.getHeight());
    }

    /**
     * Sets the x and y coordinates in this widget's local context (i.e. the parent container) as well as the width and height of the widget at the same time.
     * Calling this method has the same effect as calling {@link #setPosition(int, int)} and {@link #setSize(int, int)} separately with the four integers.
     *
     * @param x The new leftmost x coordinate in pixels.
     * @param y The new topmost y coordinate in pixels.
     * @param width The new width in pixels.
     * @param height The new height in pixels.
     * @throws IllegalArgumentException If either the given width or the given height is negative.
     */
    default public void setBounds(int x, int y, int width, int height) {

        setPosition(x, y);
        setSize(width, height);
    }

    /**
     * Sets the x and y coordinates in this widget's local context (i.e. the parent container) as well as the width and height of the widget to the bounds of the given {@link Rectangle}.
     * Calling this method has the same effect as calling {@link #setPosition(int, int)} and {@link #setSize(int, int)} separately with the four integers.
     *
     * @param bounds The rectangle which describes the new bounds of the widget.
     */
    default public void setBounds(Rectangle bounds) {

        setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    /*
     * Event handlers
     */

    /**
     * Revalidates this widget by layouting its contents again, <b>but not changing its own size!</b><br>
     * <br>
     * This method should only perform a revalidation if either the widget has been invalidated beforehand (e.g. because the size of the flexible widget has been changed) or the force flag is set.
     * If the widget is a {@link WidgetParent parent} and therefore has child widgets, it should <b>not</b> revalidate those child widgets automatically.
     *
     * @param viewport The rectangle on the screen in which the widget will be allowed to draw its stuff.
     *        This viewport is especially handy when you want to layout your widget such that nothing clips out of the screen.<br>
     *        It's important to note that the global x/y coordinates of the viewport are the origin of the coordinate system the local x/y coordinates of this widget lie in.
     *        That means that you need to translate your widget's local x/y coordinates by the global viewport x/y coordinates if you want to determine its position correctly!
     * @param force Whether the widget must be revalidated, even if it wasn't explicitly invalidated beforehand.
     * @return Whether a revalidation has been performed.
     */
    boolean revalidate(Viewport viewport, boolean force);

}

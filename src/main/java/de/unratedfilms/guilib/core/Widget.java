
package de.unratedfilms.guilib.core;

import org.lwjgl.input.Keyboard;

/**
 * Widgets are the core of this library. A widget can be a label, a text field, a button, essentially everything.
 * All controls should somehow implement this Widget interface.
 * It is recommended to use the {@link WidgetAdapter} as a superclass in order to avoid boilerplate code.
 */
public interface Widget {

    /**
     * Returns the leftmost x coordinate of this widget on the screen.
     *
     * @return The leftmost x coordinate in pixels.
     */
    public int getX();

    /**
     * Sets the leftmost x coordinate of this widget on the screen.
     *
     * @param x The new leftmost x coordinate in pixels.
     */
    public void setX(int x);

    /**
     * Returns the topmost y coordinate of this widget on the screen.
     *
     * @return The topmost y coordinate in pixels.
     */
    public int getY();

    /**
     * Sets the topmost y coordinate of this widget on the screen.
     *
     * @param y The new topmost y coordinate in pixels.
     */
    public void setY(int y);

    /**
     * Sets both the x and y coordinates of this widget at the same time.
     * Note that this is just a convenience method and internally calls {@link #setX(int)} and {@link #setY(int)}.
     * That means that you only need to override those if you want to enforce a shift on all coordinates.
     *
     * @param x The new leftmost x coordinate in pixels.
     * @param y The new topmost y coordinate in pixels.
     */
    public void setPosition(int x, int y);

    /**
     * Returns the width of this widget.
     *
     * @return The width in pixels.
     */
    public int getWidth();

    /**
     * Sets the width of this widget.
     *
     * @param width The new width in pixels.
     */
    public void setWidth(int width);

    /**
     * Returns the height of this widget.
     *
     * @return The height in pixels.
     */
    public int getHeight();

    /**
     * The new height of this widget.
     *
     * @param height The new height in pixels.
     */
    public void setHeight(int height);

    /**
     * Sets both the width and height of this widget at the same time.
     * Note that this is just a convenience method and internally calls {@link #setWidth(int)} and {@link #setHeight(int)}.
     * That means that you only need to override those if you want to enforce a shift on all lengths.
     *
     * @param width The new width in pixels.
     * @param height The new height in pixels.
     */
    public void setSize(int width, int height);

    /*
     * Event handlers
     */

    /**
     * Updates this widget during each tick.
     */
    public void update();

    /**
     * Draws this widget.
     *
     * @param mx The x coordinate of the mouse.
     * @param my The y coordinate of the mouse.
     */
    public void draw(int mx, int my);

    /**
     * Called once when the given {@link MouseButton} is pushed down.
     *
     * @param mx The x coordinate of the mouse.
     * @param my The y coordinate of the mouse.
     * @param mouseButton The pressed mouse button.
     * @return Whether the widget should be focused as a result of the click.
     */
    public boolean mousePressed(int mx, int my, MouseButton mouseButton);

    /**
     * Called once when the given {@link MouseButton} is released.
     *
     * @param mx The x coordinate of the mouse.
     * @param my The y coordinate of the mouse.
     * @param mouseButton The released mouse button.
     */
    public void mouseReleased(int mx, int my, MouseButton mouseButton);

    /**
     * Called once when the given key is typed.
     *
     * @param typedChar The typed character (if any).
     * @param keyCode {@link Keyboard}{@code .KEY_} code for the typed key.
     * @return Whether this widget has captured this keyboard event, which means that it won't cause a focus shift (tab) or a scrollbar shift (up/down).
     */
    public boolean keyTyped(char typedChar, int keyCode);

    /**
     * Called when the mouse wheel has moved.
     *
     * @param delta Clamped difference, currently either +5 or -5.
     * @return Whether this widget has captured this mouse wheel event, which means that other widget's won't get notified about the wheel rotation.
     *         Of course, this only applies if no widget is focused, because in that case only the widget in focus would be notified.
     */
    public boolean mouseWheel(int delta);

    /*
     * Predicates
     */

    /**
     * Determines whether the specified coordinate is in bounds of this widget's area.
     *
     * @param x The x coordinate of the point which should be tested.
     * @param y The y coordinate of the point which should be tested.
     * @return Whether the mouse is in the bounds of this widget.
     */
    public boolean inBounds(int x, int y);

    /**
     * Determines whether this widget should render, given the top and bottom edges of the screen.
     *
     * @param topY The top y of the screen.
     * @param bottomY The bottom y of the screen.
     * @return Whether or not this widget should be rendered.
     */
    public boolean shouldRender(int topY, int bottomY);

}

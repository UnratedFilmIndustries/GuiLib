
package de.unratedfilms.guilib.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * Widgets are the core of this library. A widget can be a label, a text field, a button, essentially everything.
 * All controls should somehow implement this Widget interface.
 * It is recommended to use one of the widget adapter superclasses in order to avoid boilerplate code.
 */
public interface Widget {

    /**
     * Returns the leftmost x coordinate of this widget in its local context (i.e. the parent container).
     *
     * @return The leftmost x coordinate in pixels.
     */
    default public int getX() {

        return getCoord(Axis.X);
    }

    /**
     * Sets the leftmost x coordinate of this widget in its local context (i.e. the parent container).
     *
     * @param x The new leftmost x coordinate in pixels.
     */
    default public void setX(int x) {

        setCoord(Axis.X, x);
    }

    /**
     * Returns the topmost y coordinate of this widget in its local context (i.e. the parent container).
     *
     * @return The topmost y coordinate in pixels.
     */
    default public int getY() {

        return getCoord(Axis.Y);
    }

    /**
     * Sets the topmost y coordinate of this widget in its local context (i.e. the parent container).
     *
     * @param y The new topmost y coordinate in pixels.
     */
    default public void setY(int y) {

        setCoord(Axis.Y, y);
    }

    /**
     * Returns the leftmost x <b>or</b> topmost y coordinate of this widget in its local context (i.e. the parent container), depending on the given {@link Axis}.
     *
     * @param axis Whether the {@link Axis#X} or {@link Axis#Y} coordinate should be returned.
     * @return The leftmost x / topmost y coordinate in pixels.
     */
    public int getCoord(Axis axis);

    /**
     * Sets the leftmost x <b>or</b> topmost y coordinate of this widget in its local context (i.e. the parent container), depending on the given {@link Axis}.
     * Calling this method has the same effect as calling either {@link #setX(int)} or {@link #setY(int)} explicitly with the coordinates value.
     *
     * @param axis Whether the {@link Axis#X} or {@link Axis#Y} coordinate should be changed.
     * @param coord The new leftmost x / topmost y coordinate in pixels.
     */
    public void setCoord(Axis axis, int coord);

    /**
     * Returns a {@link Point} which contains the x and y coordinates of this widget in its local context (i.e. the parent container).
     *
     * @return The point the widget is located at.
     */
    default public Point getPosition() {

        return new Point(getX(), getY());
    }

    /**
     * Sets both the x and y coordinates of this widget in its local context (i.e. the parent container) at the same time.
     * Calling this method has the same effect as calling {@link #setX(int)} and {@link #setY(int)} separately with the two coordinates.
     *
     * @param x The new leftmost x coordinate in pixels.
     * @param y The new topmost y coordinate in pixels.
     */
    default public void setPosition(int x, int y) {

        setX(x);
        setY(y);
    }

    /**
     * Sets the x and y coordinates of this widget in its local context (i.e. the parent container) to the coordinates of the given {@link Point}.
     * Calling this method has the same effect as calling {@link #setX(int)} and {@link #setY(int)} separately with the two coordinates.
     *
     * @param position The new point the widget should be located at.
     */
    default public void setPosition(Point position) {

        setPosition(position.getX(), position.getY());
    }

    /**
     * Returns the width of this widget.
     *
     * @return The width in pixels.
     *         Note that negative widths are not allowed.
     */
    default public int getWidth() {

        return getExtent(Axis.X);
    }

    /**
     * Returns the rightmost x coordinate of this widget in its local context (i.e. the parent container).
     * Note that this method just returns the sum of {@link #getX()} and {@link #getWidth()}.
     *
     * @return The rightmost x coordinate in pixels.
     */
    default public int getRight() {

        return getX() + getWidth();
    }

    /**
     * Returns the height of this widget.
     *
     * @return The height in pixels.
     *         Note that negative heights are not allowed.
     */
    default public int getHeight() {

        return getExtent(Axis.Y);
    }

    /**
     * Returns the lowermost y coordinate of this widget in its local context (i.e. the parent container).
     * Note that this method just returns the sum of {@link #getY()} and {@link #getHeight()}.
     *
     * @return The lowermost y coordinate in pixels.
     */
    default public int getBottom() {

        return getY() + getHeight();
    }

    /**
     * Returns the width or height of this widget, depending on whether the given {@link Axis} argument points to {@link Axis#X} or {@link Axis#Y}.
     *
     * @param axis Whether the {@link Axis#X} (width) or {@link Axis#Y} (height) extent should be returned.
     * @return The width or height in pixels.
     *         Note that negative widths and heights are not allowed.
     */
    public int getExtent(Axis axis);

    /**
     * Returns a {@link Dimension} object which contains the width and height of this widget.
     *
     * @return The size of the widget.
     */
    default public Dimension getSize() {

        return new Dimension(getWidth(), getHeight());
    }

    /**
     * Returns a {@link Rectangle} which contains the x and y coordinates of this widget in its local context (i.e. the parent container) as well as the width and height of the widget.
     *
     * @return The rectangle which contains the bounds of the widget.
     */
    default public Rectangle getBounds() {

        return new Rectangle(getPosition(), getSize());
    }

    /*
     * Event handlers
     */

    /**
     * Updates this widget during each tick.
     */
    public void update();

    public boolean revalidate(boolean force);

    /**
     * Draws this widget.
     *
     * @param viewport The rectangle on the screen in which the widget is allowed to draw its stuff.
     *        It's important to note that the global x/y coordinates of the viewport are the origin of the coordinate system the local x/y coordinates of this widget lie in.
     *        That means that you need to translate your widget's local x/y coordinates by the global viewport x/y coordinates if you want to draw correctly!<br>
     *        <br>
     *        Please also remember to use the {@link GL11#GL_SCISSOR_TEST} in order to ensure you are really only drawing inside the viewport rectangle.
     *        Note, however, that there might be occasions where you deliberately ignore the viewport boundary,
     *        i.e. because you want to draw a dropdown menu that of course shouldn't be cut of by a scrolling container.
     * @param mx The global x coordinate of the mouse on the screen.
     * @param my The global y coordinate of the mouse on the screen.
     */
    public void draw(Viewport viewport, int mx, int my);

    /**
     * Called once when the given {@link MouseButton} is pushed down.
     * If any widget is focused, that widget will be the first to get notified about the click.
     *
     * @param viewport The rectangle on the screen in which the widget is allowed to receive mouse inputs.
     *        It's important to note that the global x/y coordinates of the viewport are the origin of the coordinate system the local x/y coordinates of this widget lie in.
     *        That means that you need to translate your widget's local x/y coordinates by the global viewport x/y coordinates before you compare with the given global mouse coordinates!<br>
     *        <br>
     *        Please remember to use {@link Viewport#inGlobalBounds(int, int)} or {@link Viewport#inLocalBounds(int, int)} to check whether you are actually allowed to take mouse input
     *        from the given coordinates.
     *        Note, however, that there might be occasions where you deliberately ignore the viewport boundary,
     *        i.e. because you want to implement a dropdown menu that of course shouldn't be cut of by a scrolling container.
     * @param mx The global x coordinate of the mouse on the screen.
     * @param my The global y coordinate of the mouse on the screen.
     * @param mouseButton The pressed mouse button.
     * @return Whether this widget has captured this click, which means that no other widget will be notified about it.
     */
    public boolean mousePressed(Viewport viewport, int mx, int my, MouseButton mouseButton);

    /**
     * Called once when the given {@link MouseButton} is released after this widget has captured it being pressed by returning {@code true} in {@link #mousePressed(Viewport, int, int, MouseButton)}.
     *
     * @param viewport The global x/y coordinates of the viewport are the origin of the coordinate system the local x/y coordinates of this widget lie in.
     *        That means that you need to translate your widget's local x/y coordinates by the global viewport x/y coordinates before you compare with the given global mouse coordinates!
     *        Note that you don't need to do any fancy boundary checking like in {@link #mousePressed(Viewport, int, int, MouseButton)} since a mouse release event is only fired if a pressed event has
     *        been captured by this widget beforehand.
     * @param mx The global x coordinate of the mouse on the screen.
     * @param my The global y coordinate of the mouse on the screen.
     * @param mouseButton The released mouse button.
     */
    public void mouseReleased(Viewport viewport, int mx, int my, MouseButton mouseButton);

    /**
     * Called when the mouse wheel has moved.
     * If any widget is focused, that widget will be the first to get notified about the wheel rotation.
     *
     * @param viewport The rectangle on the screen in which the widget is allowed to receive mouse inputs.
     *        It's important to note that the global x/y coordinates of the viewport are the origin of the coordinate system the local x/y coordinates of this widget lie in.
     *        That means that you need to translate your widget's local x/y coordinates by the global viewport x/y coordinates before you compare with the given global mouse coordinates!<br>
     *        <br>
     *        Please remember to use {@link Viewport#inGlobalBounds(int, int)} or {@link Viewport#inLocalBounds(int, int)} to check whether you are actually allowed to take mouse input
     *        from the given coordinates.
     *        Note, however, that there might be occasions where you deliberately ignore the viewport boundary,
     *        i.e. because you want to implement a dropdown menu that of course shouldn't be cut of by a scrolling container.
     * @param mx The global x coordinate of the mouse on the screen.
     * @param my The global y coordinate of the mouse on the screen.
     * @param delta Clamped difference, currently either +5 or -5.
     * @return Whether this widget has captured this wheel rotation event, which means that no other widget will be notified about it.
     *         Returning {@code true} also prevents the event from causing a scrollbar shift.
     */
    public boolean mouseWheel(Viewport viewport, int mx, int my, int delta);

    /**
     * Called once when the given key is typed.
     * If any widget is focused, that widget will be the first to get notified about the keyboard event.
     *
     * @param typedChar The typed character (if any).
     * @param keyCode {@link Keyboard}{@code .KEY_} code for the typed key.
     * @return Whether this widget has captured this keyboard event, which means that no other widget will be notified about it.
     *         Returning {@code true} also prevents the keyboard event from causing a focus shift (tab) or a scrollbar shift (up/down).
     */
    public boolean keyTyped(char typedChar, int keyCode);

    /*
     * Predicates
     */

    /**
     * Determines whether the specified <b>local</b> coordinate is in bounds of this widget's area, <b>considering the given {@link Viewport}</b>.
     *
     * @param viewport The rectangle on the screen in which the widget is allowed to operate.
     *        Typically, you receive a viewport object from somewhere, which you can then provide to this method.
     * @param lx The local x coordinate of the point which should be tested.
     * @param ly The local y coordinate of the point which should be tested.
     * @return Whether the given local coordinate is in the bounds of this widget and the given viewport.
     */
    default public boolean inLocalBounds(Viewport viewport, int lx, int ly) {

        return viewport.inLocalBounds(lx, ly) && lx >= getX() && ly >= getY() && lx < getX() + getWidth() && ly < getY() + getHeight();
    }

    /**
     * Determines whether the specified <b>global</b> coordinate is in bounds of this widget's area, <b>considering the given {@link Viewport}</b>.
     *
     * @param viewport The rectangle on the screen in which the widget is allowed to operate.
     *        Typically, you receive a viewport object from somewhere, which you can then provide to this method.
     * @param gx The global x coordinate of the point which should be tested.
     * @param gy The global y coordinate of the point which should be tested.
     * @return Whether the given global coordinate is in the bounds of this widget and the given viewport.
     */
    default public boolean inGlobalBounds(Viewport viewport, int gx, int gy) {

        return inLocalBounds(viewport, viewport.localX(gx), viewport.localY(gy));
    }

}

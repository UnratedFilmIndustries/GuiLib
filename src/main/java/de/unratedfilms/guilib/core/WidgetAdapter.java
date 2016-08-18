
package de.unratedfilms.guilib.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * A default implementation of {@link Widget} which provides most of the standard methods and extends {@link Gui} so that users gain access to some drawing methods.
 */
public abstract class WidgetAdapter extends Gui implements Widget {

    // Utility constant for quick access
    protected static final Minecraft MC = Minecraft.getMinecraft();

    private int                      x, y;
    private int                      width, height;

    /**
     * Creates a new widget.
     *
     * @param width The width of this widget in pixels.
     * @param height The height of this widget in pixels.
     */
    public WidgetAdapter(int width, int height) {

        this.width = width;
        this.height = height;
    }

    /**
     * Creates a new widget at the given coordinates.
     *
     * @param x The leftmost x coordinate of this widget on the screen in pixels.
     * @param y The topmost y coordinate of this widget on the screen in pixels.
     * @param width The width of this widget in pixels.
     * @param height The height of this widget in pixels.
     */
    public WidgetAdapter(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getX() {

        return x;
    }

    @Override
    public void setX(int x) {

        this.x = x;
    }

    @Override
    public int getY() {

        return y;
    }

    @Override
    public void setY(int y) {

        this.y = y;
    }

    @Override
    public void setPosition(int x, int y) {

        setX(x);
        setY(y);
    }

    @Override
    public int getWidth() {

        return width;
    }

    @Override
    public void setWidth(int width) {

        this.width = width;
    }

    @Override
    public int getHeight() {

        return height;
    }

    @Override
    public void setHeight(int height) {

        this.height = height;
    }

    @Override
    public void setSize(int width, int height) {

        setWidth(width);
        setHeight(height);
    }

    /*
     * Empty event handlers
     */

    @Override
    public void update() {

    }

    @Override
    public boolean mousePressed(int mx, int my, MouseButton mouseButton) {

        return false;
    }

    @Override
    public void mouseReleased(int mx, int my, MouseButton mouseButton) {

    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {

        return false;
    }

    @Override
    public boolean mouseWheel(int delta) {

        return false;
    }

    /*
     * Standard predicates
     */

    @Override
    public boolean inBounds(int testX, int testY) {

        return testX >= x && testY >= y && testX < x + width && testY < y + height;
    }

    @Override
    public boolean shouldRender(int topY, int bottomY) {

        return y + height >= topY && y <= bottomY;
    }

}

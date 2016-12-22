
package de.unratedfilms.guilib.core;

import org.apache.commons.lang3.Validate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * A default implementation of {@link Widget} which provides most of the standard methods and extends {@link Gui} so that users gain access to some drawing methods.
 */
public abstract class WidgetAdapter extends Gui implements Widget {

    // Utility constant for quick access
    protected static final Minecraft MC     = Minecraft.getMinecraft();

    private Rectangle                bounds = new Rectangle(0, 0, 0, 0);
    protected boolean                valid  = false;

    @Override
    public int getX() {

        return bounds.getX();
    }

    @Override
    public void setX(int x) {

        bounds = bounds.withX(x);
    }

    @Override
    public int getY() {

        return bounds.getY();
    }

    @Override
    public void setY(int y) {

        bounds = bounds.withY(y);
    }

    @Override
    public int getCoord(Axis axis) {

        return bounds.getCoord(axis);
    }

    @Override
    public void setCoord(Axis axis, int coord) {

        bounds = bounds.withCoord(axis, coord);
    }

    @Override
    public Point getPosition() {

        return bounds.getPosition();
    }

    @Override
    public void setPosition(int x, int y) {

        bounds = bounds.withPosition(x, y);
    }

    @Override
    public void setPosition(Point position) {

        bounds = bounds.withPosition(position);
    }

    @Override
    public int getWidth() {

        return bounds.getWidth();
    }

    @Override
    public int getRight() {

        return getX() + getWidth();
    }

    public void setWidth(int width) {

        Validate.isTrue(width >= 0, "Width of widget must not be negative");
        bounds = bounds.withWidth(width);
    }

    @Override
    public int getHeight() {

        return bounds.getHeight();
    }

    @Override
    public int getBottom() {

        return getY() + getHeight();
    }

    public void setHeight(int height) {

        Validate.isTrue(height >= 0, "Height of widget must not be negative");
        bounds = bounds.withHeight(height);
    }

    @Override
    public int getExtent(Axis axis) {

        return bounds.getExtent(axis);
    }

    public void setExtent(Axis axis, int extent) {

        Validate.isTrue(extent >= 0, "Width/height of widget must not be negative");
        bounds = bounds.withExtent(axis, extent);
    }

    @Override
    public Dimension getSize() {

        return bounds.getSize();
    }

    public void setSize(int width, int height) {

        bounds = bounds.withSize(width, height);
    }

    public void setSize(Dimension size) {

        bounds = bounds.withSize(size);
    }

    @Override
    public Rectangle getBounds() {

        return bounds;
    }

    public void setBounds(int x, int y, int width, int height) {

        bounds = new Rectangle(x, y, width, height);
    }

    public void setBounds(Rectangle bounds) {

        this.bounds = bounds;
    }

    /**
     * Marks this very widget as invalid and thus causes a revalidation to be run prior to the next render cycle.
     * That means that the widget will layout itself again.
     * In case of a {@link WidgetRigid rigid widget}, the widget will also readjust its own bounds.
     */
    protected void invalidate() {

        valid = false;
    }

    @Override
    public boolean revalidate(boolean force) {

        if (!valid || force) {
            doRevalidate();
            valid = true;
            return true;
        } else {
            return false;
        }
    }

    /*
     * Empty event handlers
     */

    protected void doRevalidate() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Viewport viewport, int mx, int my) {

    }

    @Override
    public boolean mousePressed(Viewport viewport, int mx, int my, MouseButton mouseButton) {

        return false;
    }

    @Override
    public void mouseReleased(Viewport viewport, int mx, int my, MouseButton mouseButton) {

    }

    @Override
    public boolean mouseWheel(Viewport viewport, int mx, int my, int delta) {

        return false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {

        return false;
    }

    /*
     * Standard predicates
     */

    @Override
    public boolean inLocalBounds(Viewport viewport, int lx, int ly) {

        return viewport.inLocalBounds(lx, ly) && lx >= getX() && ly >= getY() && lx < getX() + getWidth() && ly < getY() + getHeight();
    }

    @Override
    public boolean inGlobalBounds(Viewport viewport, int gx, int gy) {

        return inLocalBounds(viewport, viewport.localX(gx), viewport.localY(gy));
    }

}

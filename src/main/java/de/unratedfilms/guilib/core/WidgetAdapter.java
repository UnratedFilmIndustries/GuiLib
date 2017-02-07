
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
    public int getCoord(Axis axis) {

        return bounds.getCoord(axis);
    }

    @Override
    public void setCoord(Axis axis, int coord) {

        bounds = bounds.withCoord(axis, coord);
    }

    public void setWidth(int width) {

        Validate.isTrue(width >= 0, "Width of widget must not be negative");
        bounds = bounds.withWidth(width);
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

        Validate.isTrue(extent >= 0, (axis == Axis.X ? "Width" : "Height") + " of widget must not be negative");
        bounds = bounds.withExtent(axis, extent);
    }

    public void setSize(int width, int height) {

        bounds = bounds.withSize(width, height);
    }

    public void setSize(Dimension size) {

        bounds = bounds.withSize(size);
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

    public boolean revalidate(boolean force) {

        return revalidate(null, force);
    }

    public boolean revalidate(Viewport viewport, boolean force) {

        if (!valid || force) {
            revalidateThis();
            revalidateThis(viewport);
            valid = true;
            return true;
        } else {
            return false;
        }
    }

    /*
     * Empty event handlers
     */

    /**
     * Called when this widget needs to be revalidated.
     * An {@link #invalidate() invalid} and a force check have already been performed.
     */
    protected void revalidateThis() {

    }

    /**
     * <b>Only used in {@link WidgetFlexible flexible widgets}!</b> Called when this widget needs to be revalidated.
     * An {@link #invalidate() invalid} and a force check have already been performed.
     *
     * @param viewport The rectangle on the screen in which the widget will be allowed to draw its stuff.
     *        This viewport is especially handy when you want to layout your widget such that nothing clips out of the screen.<br>
     *        It's important to note that the global x/y coordinates of the viewport are the origin of the coordinate system the local x/y coordinates of this widget lie in.
     *        That means that you need to translate your widget's local x/y coordinates by the global viewport x/y coordinates if you want to determine its position correctly!
     */
    protected void revalidateThis(Viewport viewport) {

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

}

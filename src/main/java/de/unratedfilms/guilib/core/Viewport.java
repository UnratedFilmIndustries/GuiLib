
package de.unratedfilms.guilib.core;

import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class Viewport {

    private static final Minecraft MC = Minecraft.getMinecraft();

    private final Dimension        screenSize;
    private final Point            widgetOffset;
    private final Rectangle        scissor;

    public Viewport(Dimension screenSize) {

        this(screenSize, new Point(0, 0));
    }

    public Viewport(Dimension screenSize, Point widgetOffset) {

        this(screenSize, widgetOffset, new Rectangle(0, 0, screenSize));
    }

    public Viewport(Dimension screenSize, Rectangle scissor) {

        this(screenSize, new Point(0, 0), scissor);
    }

    public Viewport(Dimension screenSize, Point widgetOffset, Rectangle scissor) {

        Validate.notNull(screenSize, "Screen size of viewport must no be null");
        Validate.notNull(widgetOffset, "Widget offset of viewport must no be null");
        Validate.notNull(scissor, "Scissor rectangle of viewport must no be null");

        this.screenSize = screenSize;
        this.widgetOffset = widgetOffset;
        this.scissor = scissor;
    }

    public Dimension getScreenSize() {

        return screenSize;
    }

    public Point getWidgetOffset() {

        return widgetOffset;
    }

    public Viewport withWidgetOffset(Point newWidgetOffset) {

        return new Viewport(screenSize, newWidgetOffset, scissor);
    }

    public Viewport withoutWidgetOffset() {

        return new Viewport(screenSize, new Point(0, 0), scissor);
    }

    public Rectangle getScissor() {

        return scissor;
    }

    public Viewport withScissor(Rectangle newScissor) {

        return new Viewport(screenSize, widgetOffset, newScissor);
    }

    public Viewport withoutScissor() {

        return new Viewport(screenSize, widgetOffset, new Rectangle(0, 0, screenSize));
    }

    public int localX(int globalX) {

        return globalX - widgetOffset.getX();
    }

    public int globalX(int localX) {

        return localX + widgetOffset.getX();
    }

    public int localY(int globalY) {

        return globalY - widgetOffset.getY();
    }

    public int globalY(int localY) {

        return localY + widgetOffset.getY();
    }

    public Point localPosition(Point globalPosition) {

        return new Point(localX(globalPosition.getX()), localY(globalPosition.getY()));
    }

    public Point globalPosition(Point localPosition) {

        return new Point(globalX(localPosition.getX()), globalY(localPosition.getY()));
    }

    /**
     * Determines whether the specified local coordinate (relative to this viewport's widget offset) is in bounds of the viewport's scissor area.
     *
     * @param lx The local x coordinate of the point which should be tested.
     * @param ly The local y coordinate of the point which should be tested.
     * @return Whether the given local coordinate is in the bounds of the viewport.
     */
    public boolean inLocalBounds(int lx, int ly) {

        return inGlobalBounds(globalX(lx), globalY(ly));
    }

    /**
     * Determines whether the specified global coordinate is in bounds of the viewport's scissor area.
     *
     * @param gx The global x coordinate of the point which should be tested.
     * @param gy The global y coordinate of the point which should be tested.
     * @return Whether the given global coordinate is in the bounds of the viewport.
     */
    public boolean inGlobalBounds(int gx, int gy) {

        return scissor.inBounds(gx, gy);
    }

    public void drawInLocalContext(int mx, int my, Drawer drawer) {

        // Enable translation by the widget offset
        GlStateManager.pushMatrix();
        GlStateManager.translate(widgetOffset.getX(), widgetOffset.getY(), 0);

        // Enable scissors
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int scale = new ScaledResolution(MC).getScaleFactor();
        GL11.glScissor(scissor.getX() * scale, MC.displayHeight - scissor.getY() * scale - scissor.getHeight() * scale, scissor.getWidth() * scale, scissor.getHeight() * scale);

        // Actually draw whatever needs to be drawn
        drawer.draw(localX(mx), localY(my));

        // Disable scissors
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        // Disable translation
        GlStateManager.popMatrix();
    }

    public static interface Drawer {

        public void draw(int lmx, int lmy);

    }

}

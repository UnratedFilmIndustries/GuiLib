
package de.unratedfilms.guilib.widgets.view.impl;

import java.util.List;
import org.lwjgl.opengl.GL11;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiUtils;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetParent;
import de.unratedfilms.guilib.widgets.model.ColorPicker;
import de.unratedfilms.guilib.widgets.view.adapters.ColorPickerAdapter;

/**
 * Default style {@link ColorPicker}.
 */
public class ColorPickerImpl extends ColorPickerAdapter implements WidgetParent {

    // The amount of pixels between the button border and the actual color preview
    private static final int              COLOR_PREVIEW_PADDING     = 3;

    private static final int              EXT_MARGIN                = 10;

    private static final ResourceLocation TEXTURE_VANILLA_WIDGETS   = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation TEXTURE_CHECKERBOARD_TILE = new ResourceLocation("guilib", "textures/gui/checkerboard_tile.png");

    protected static void drawCheckerboard(int left, int top, int right, int bottom) {

        Tessellator tes = Tessellator.getInstance();
        VertexBuffer buf = tes.getBuffer();

        MC.getTextureManager().bindTexture(TEXTURE_CHECKERBOARD_TILE);
        GlStateManager.color(1, 1, 1, 1);

        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        double u = Math.ceil( (right - left) / 8d) / 2d;
        double v = Math.ceil( (bottom - top) / 8d) / 2d;
        buf.pos(left, bottom, 0).tex(0, v).endVertex();
        buf.pos(right, bottom, 0).tex(u, v).endVertex();
        buf.pos(right, top, 0).tex(u, 0).endVertex();
        buf.pos(left, top, 0).tex(0, 0).endVertex();

        tes.draw();
    }

    // The actual color picker that appears when you click on this button
    private final ColorPickerImplExt ext = new ColorPickerImplExt(this);

    public ColorPickerImpl() {

        setSize(20, 20); // default size
        setColorBits(0xAAFFCC33);
    }

    /*
     * Event handlers
     */

    @Override
    public void focusGained() {

        super.focusGained();

        // The ext has just been opened, so we need to make sure to edit the correct color
        ext.pullColorFromParent();
    }

    @Override
    protected void revalidateThis(Viewport viewport) {

        // Make sure that ext does not peek out of the right side of the Minecraft window
        int maxX = viewport.localX(viewport.getScreenSize().getWidth() - EXT_MARGIN - ext.getWidth());
        ext.setX(MathHelper.clamp(getX(), 0, maxX));

        // Make sure that ext does not peek out of the lower side of the Minecraft window
        int maxY = viewport.localY(viewport.getScreenSize().getHeight() - EXT_MARGIN - ext.getHeight());
        ext.setY(MathHelper.clamp(getY(), 0, maxY));
    }

    /*
     * Drawing code
     */

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        boolean hover = inLocalBounds(viewport, lmx, lmy);

        // Background: Button
        {
            int u = 0, v = 46 + (hover || isFocused() ? 40 : 20);
            GuiUtils.drawContinuousTexturedBox(TEXTURE_VANILLA_WIDGETS, getX(), getY(), u, v, getWidth(), getHeight(), 200, 20, 2, 3, 2, 2, zLevel);
        }

        // Foreground: Selected color
        {
            drawCheckerboard(getX() + COLOR_PREVIEW_PADDING, getY() + COLOR_PREVIEW_PADDING, getRight() - COLOR_PREVIEW_PADDING, getBottom() - COLOR_PREVIEW_PADDING);
            drawRect(getX() + COLOR_PREVIEW_PADDING, getY() + COLOR_PREVIEW_PADDING, getRight() - COLOR_PREVIEW_PADDING, getBottom() - COLOR_PREVIEW_PADDING, getColorBits());
        }
    }

    /*
     * Forwarding events to ext, removing the scissor area
     */

    @Override
    public List<Widget> getChildren() {

        return isFocused() ? ImmutableList.of(ext) : ImmutableList.of();
    }

    @Override
    public Viewport getChildViewport(Viewport viewport, Widget child) {

        if (child == ext) {
            return viewport.withoutScissor();
        }

        throw new UnknownChildException(this, child);
    }

}

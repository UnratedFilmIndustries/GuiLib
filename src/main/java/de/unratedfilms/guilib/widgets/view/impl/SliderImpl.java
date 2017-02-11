
package de.unratedfilms.guilib.widgets.view.impl;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.widgets.view.adapters.SliderAdapter;

/**
 * Vanilla GuiSlider in Widget form, but still abstract and without an implemented value type {@code <V>}.
 * See {@link SliderDecimalImpl} and {@link SliderIntegerImpl} for concrete implementations.
 *
 * @param <V> The type of number that can be chosen using the slider (e.g. int or float).
 */
public abstract class SliderImpl<V> extends SliderAdapter<V> {

    private static final ResourceLocation TEXTURE     = new ResourceLocation("textures/gui/widgets.png");

    private static final int              HANDLE_SIZE = 8;

    public SliderImpl(V minValue, V maxValue, SliderLabelFormatter<V> textFormatter, V value) {

        super(minValue, maxValue, textFormatter, value);

        setSize(150, 20);
    }

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        super.drawInLocalContext(viewport, lmx, lmy);

        // Rail
        GuiUtils.drawContinuousTexturedBox(TEXTURE, getX(), getY(), 0, 46, getWidth(), getHeight(), 200, 20, 2, 3, 2, 2, zLevel);

        // Handle
        int handleCoord = getCoord(getDraggingAxis()) + (int) (getDegree() * (getExtent(getDraggingAxis()) - HANDLE_SIZE));
        if (getDraggingAxis() == Axis.X) {
            GuiUtils.drawContinuousTexturedBox(TEXTURE, handleCoord, getY(), 0, 66, HANDLE_SIZE, getHeight(), 200, 20, 2, 3, 2, 2, zLevel);
        } else {
            GuiUtils.drawContinuousTexturedBox(TEXTURE, getX(), handleCoord, 0, 66, getWidth(), HANDLE_SIZE, 200, 20, 2, 3, 2, 2, zLevel);
        }

        // Label
        String label = getLabelFormatter().formatLabel(this);
        int color = inLocalBounds(viewport, lmx, lmy) ? 16777120 : 0xffffff;
        if (getDraggingAxis() == Axis.X) {
            drawCenteredString(MC.fontRendererObj, label, getX() + getWidth() / 2, getY() + (getHeight() - MC.fontRendererObj.FONT_HEIGHT) / 2, color);
        } else {
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(getX() + (getWidth() - MC.fontRendererObj.FONT_HEIGHT) / 2, getY() + getHeight() / 2, 0);
                GlStateManager.rotate(-90, 0, 0, 1);
                drawCenteredString(MC.fontRendererObj, label, 0, 0, color);
            }
            GlStateManager.popMatrix();
        }
    }

}

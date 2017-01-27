
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.widgets.view.adapters.SliderAdapter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Vanilla GuiSlider in Widget form, but still abstract and without an implemented value type {@code <V>}.
 * See {@link SliderDecimalImpl} and {@link SliderIntegerImpl} for concrete implementations.
 *
 * @param <V> The type of number that can be chosen using the slider (e.g. int or float).
 */
public abstract class SliderImpl<V> extends SliderAdapter<V> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    public SliderImpl(V minValue, V maxValue, SliderLabelFormatter<V> textFormatter, V value) {

        super(minValue, maxValue, textFormatter, value);

        setSize(150, 20);
    }

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        super.drawInLocalContext(viewport, lmx, lmy);

        MC.renderEngine.bindTexture(TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Rail
        drawTexturedModalRect(getX(), getY(), 0, 46, getWidth() / 2, getHeight()); // left part
        drawTexturedModalRect(getX() + getWidth() / 2, getY(), 200 - getWidth() / 2, 46, getWidth() / 2, getHeight()); // right part

        // Handle
        drawTexturedModalRect(getX() + (int) (getDegree() * (getWidth() - 8)), getY(), 0, 66, 4, 20); // left part, 4 pixels in width
        drawTexturedModalRect(getX() + (int) (getDegree() * (getWidth() - 8)) + 4, getY(), 196, 66, 4, 20); // right part, 4 pixels in width

        // Label
        drawCenteredString(MC.fontRendererObj, getLabelFormatter().formatLabel(this), getX() + getWidth() / 2, getY() + (getHeight() - 8) / 2,
                inLocalBounds(viewport, lmx, lmy) ? 16777120 : 0xffffff);
    }

}

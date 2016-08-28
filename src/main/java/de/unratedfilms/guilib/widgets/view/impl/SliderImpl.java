
package de.unratedfilms.guilib.widgets.view.impl;

import org.lwjgl.opengl.GL11;
import net.minecraft.util.ResourceLocation;
import de.unratedfilms.guilib.widgets.view.adapters.SliderAdapter;

/**
 * Vanilla GuiSlider in Widget form, but still abstract and without an implemented value type {@code <V>}.
 * See {@link SliderDecimalImpl} and {@link SliderIntegerImpl} for concrete implementations.
 *
 * @param <V> The type of number that can be chosen using the slider (e.g. int or float).
 */
public abstract class SliderImpl<V> extends SliderAdapter<V> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    public SliderImpl(V minValue, V maxValue, SliderLabelFormatter<V> textFormatter, V value) {

        this(150, 20, minValue, maxValue, textFormatter, value);
    }

    public SliderImpl(int width, int height, V minValue, V maxValue, SliderLabelFormatter<V> textFormatter, V value) {

        super(width, height, minValue, maxValue, textFormatter, value);
    }

    @Override
    public void draw(int mx, int my) {

        super.draw(mx, my);

        MC.renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // Rail
        drawTexturedModalRect(getX(), getY(), 0, 46, getWidth() / 2, getHeight()); // left part
        drawTexturedModalRect(getX() + getWidth() / 2, getY(), 200 - getWidth() / 2, 46, getWidth() / 2, getHeight()); // right part

        // Handle
        drawTexturedModalRect(getX() + (int) (getDegree() * (getWidth() - 8)), getY(), 0, 66, 4, 20); // left part, 4 pixels in width
        drawTexturedModalRect(getX() + (int) (getDegree() * (getWidth() - 8)) + 4, getY(), 196, 66, 4, 20); // right part, 4 pixels in width

        // Label
        drawCenteredString(MC.fontRenderer, getLabelFormatter().formatLabel(this), getX() + getWidth() / 2, getY() + (getHeight() - 8) / 2,
                inBounds(mx, my) ? 16777120 : 0xffffff);
    }

}


package de.unratedfilms.guilib.widgets.view.impl;

import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.ResourceLocation;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.view.adapters.ButtonAdapter;

/**
 * Vanilla GuiButton in Widget form.
 */
public class ButtonLabelImpl extends ButtonAdapter implements ButtonLabel {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    private String                        label;

    public ButtonLabelImpl(String label, ButtonHandler handler) {

        this(200, 20, label, handler);
    }

    public ButtonLabelImpl(int width, int height, String label, ButtonHandler handler) {

        super(width, height, handler);

        this.label = label;
    }

    @Override
    public String getLabel() {

        return label;
    }

    @Override
    public void setLabel(String label) {

        Validate.notNull(label, "Vanilla button label cannot be null");
        this.label = label;
    }

    @Override
    public void draw(int mx, int my) {

        MC.renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean hover = inBounds(mx, my);
        int u = 0, v = 46 + getStateOffset(hover);

        if (getWidth() == 200 && getHeight() == 20) {
            drawTexturedModalRect(getX(), getY(), u, v, getWidth(), getHeight());
        } else {
            drawTexturedModalRect(getX(), getY(), u, v, getWidth() / 2, getHeight() / 2);
            drawTexturedModalRect(getX() + getWidth() / 2, getY(), u + 200 - getWidth() / 2, v, getWidth() / 2, getHeight() / 2);
            drawTexturedModalRect(getX(), getY() + getHeight() / 2, u, v + 20 - getHeight() / 2, getWidth() / 2, getHeight() / 2);
            drawTexturedModalRect(getX() + getWidth() / 2, getY() + getHeight() / 2, u + 200 - getWidth() / 2, v + 20 - getHeight() / 2, getWidth() / 2, getHeight() / 2);
        }
        drawCenteredString(MC.fontRenderer, label, getX() + getWidth() / 2, getY() + (getHeight() - 8) / 2, getTextColor(hover));
    }

    private int getStateOffset(boolean hover) {

        return isEnabled() ? hover ? 40 : 20 : 0;
    }

    private int getTextColor(boolean hover) {

        return isEnabled() ? hover ? 16777120 : 14737632 : 6250336;
    }

}

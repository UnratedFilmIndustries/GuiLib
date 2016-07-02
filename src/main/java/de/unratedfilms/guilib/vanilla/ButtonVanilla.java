
package de.unratedfilms.guilib.vanilla;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import de.unratedfilms.guilib.core.Button;

/**
 * Vanilla GuiButton in Widget form.
 */
public class ButtonVanilla extends Button {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    protected String                      label;

    public ButtonVanilla(int width, int height, String label, ButtonHandler handler) {

        super(width, height, handler);

        this.label = label;
    }

    public ButtonVanilla(String label, ButtonHandler handler) {

        this(200, 20, label, handler);
    }

    @Override
    public void draw(int mx, int my) {

        MC.renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean hover = inBounds(mx, my);
        int u = 0, v = 46 + getStateOffset(hover);

        if (width == 200 && height == 20) {
            drawTexturedModalRect(x, y, u, v, width, height);
        } else {
            drawTexturedModalRect(x, y, u, v, width / 2, height / 2);
            drawTexturedModalRect(x + width / 2, y, u + 200 - width / 2, v, width / 2, height / 2);
            drawTexturedModalRect(x, y + height / 2, u, v + 20 - height / 2, width / 2, height / 2);
            drawTexturedModalRect(x + width / 2, y + height / 2, u + 200 - width / 2, v + 20 - height / 2, width / 2, height / 2);
        }
        drawCenteredString(MC.fontRenderer, label, x + width / 2, y + (height - 8) / 2, getTextColor(hover));
    }

    private int getStateOffset(boolean hover) {

        return enabled ? hover ? 40 : 20 : 0;
    }

    private int getTextColor(boolean hover) {

        return enabled ? hover ? 16777120 : 14737632 : 6250336;
    }

    @Override
    public String getLabel() {

        return label;
    }

    @Override
    public void setLabel(String label) {

        this.label = label;
    }

    @Override
    public void handleClick(int mx, int my) {

        MC.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
        super.handleClick(mx, my);
    }

}

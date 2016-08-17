
package de.unratedfilms.guilib.vanilla;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import de.unratedfilms.guilib.core.Checkbox;
import de.unratedfilms.guilib.core.MouseButton;

/**
 * Default style checkbox.
 */
public class CheckboxVanilla extends Checkbox {

    public static final int               SIZE    = 10;
    private static final ResourceLocation TEXTURE = new ResourceLocation("guilib", "textures/gui/checkbox.png");

    public CheckboxVanilla(String text) {

        this(text, false);
    }

    public CheckboxVanilla(String text, boolean checked) {

        super(SIZE + 2 + Minecraft.getMinecraft().fontRenderer.getStringWidth(text), SIZE, text, checked);
    }

    @Override
    public void handleClick(int mx, int my, MouseButton mouseButton) {

        super.handleClick(mx, my, mouseButton);
        MC.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }

    @Override
    public void draw(int mx, int my) {

        MC.renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(x, y, 0, isChecked() ? SIZE : 0, SIZE, SIZE);
        MC.fontRenderer.drawStringWithShadow(getLabel(), x + SIZE + 2, y + 1, inBounds(mx, my) ? 16777120 : 0xffffff);
    }

}

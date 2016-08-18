
package de.unratedfilms.guilib.widgets.view.impl;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import de.unratedfilms.guilib.widgets.model.Checkbox;
import de.unratedfilms.guilib.widgets.view.adapters.CheckboxAdapter;

/**
 * Default style {@link Checkbox}.
 */
public class CheckboxImpl extends CheckboxAdapter {

    private static final int              SIZE    = 10;
    private static final ResourceLocation TEXTURE = new ResourceLocation("guilib", "textures/gui/checkbox.png");

    public CheckboxImpl(String label) {

        this(label, false);
    }

    public CheckboxImpl(String label, boolean checked) {

        // Note that the width will be adjusted by the setLabel() method
        super(0, SIZE, label, checked);
    }

    @Override
    public void setLabel(String label) {

        super.setLabel(label);

        setSize(SIZE + 2 + MC.fontRenderer.getStringWidth(label), SIZE);
    }

    @Override
    public void draw(int mx, int my) {

        MC.renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(getX(), getY(), 0, isChecked() ? SIZE : 0, SIZE, SIZE);
        MC.fontRenderer.drawStringWithShadow(getLabel(), getX() + SIZE + 2, getY() + 1, inBounds(mx, my) ? 16777120 : 0xffffff);
    }

}

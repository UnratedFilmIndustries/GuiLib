
package de.unratedfilms.guilib.widgets.view.impl;

import org.apache.commons.lang3.Validate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.util.FontUtils;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.view.adapters.ButtonAdapter;

/**
 * Vanilla GuiButton in Widget form.
 */
public class ButtonLabelImpl extends ButtonAdapter implements ButtonLabel {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    private String                        label;

    public ButtonLabelImpl(String label, ButtonHandler handler) {

        super(handler);

        setSize(200, 20);
        this.label = label;
    }

    @Override
    public String getLabel() {

        return label;
    }

    @Override
    public ButtonLabelImpl setLabel(String label) {

        Validate.notNull(label, "Vanilla button label cannot be null");
        this.label = label;

        return this;
    }

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        boolean hover = inLocalBounds(viewport, lmx, lmy);

        int u = 0, v = 46 + getStateOffset(hover);
        GuiUtils.drawContinuousTexturedBox(TEXTURE, getX(), getY(), u, v, getWidth(), getHeight(), 200, 20, 2, 3, 2, 2, zLevel);

        String actualLabel = FontUtils.abbreviateIfTooLong(MC.fontRendererObj, label, getWidth() - 6);
        drawCenteredString(MC.fontRendererObj, actualLabel, getX() + getWidth() / 2, getY() + (getHeight() - MC.fontRendererObj.FONT_HEIGHT) / 2, getTextColor(hover));
    }

    private int getStateOffset(boolean hover) {

        return isEnabled() ? hover ? 40 : 20 : 0;
    }

    private int getTextColor(boolean hover) {

        return isEnabled() ? hover ? 16777120 : 14737632 : 6250336;
    }

}

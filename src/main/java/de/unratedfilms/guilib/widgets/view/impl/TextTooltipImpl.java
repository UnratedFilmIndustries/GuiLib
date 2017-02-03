
package de.unratedfilms.guilib.widgets.view.impl;

import java.util.List;
import org.apache.commons.lang3.Validate;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.util.FontUtils;
import de.unratedfilms.guilib.widgets.model.TextTooltip;
import de.unratedfilms.guilib.widgets.view.adapters.TextTooltipAdapter;

/**
 * The vanilla {@link TextTooltip} you can see whenever you're hovering over something in Widget form.
 */
public class TextTooltipImpl extends TextTooltipAdapter {

    private FontRenderer font;

    public TextTooltipImpl(List<String> lines) {

        this(lines, MC.fontRendererObj);
    }

    public TextTooltipImpl(List<String> lines, FontRenderer font) {

        super(lines);

        setFont(font);
    }

    public FontRenderer getFont() {

        return font;
    }

    public void setFont(FontRenderer font) {

        Validate.notNull("Vanilla tooltip cannot have a null font");
        this.font = font;
        invalidate();
    }

    @Override
    protected void revalidateThis() {

        setSize(FontUtils.getMaxStringWidth(font, getLines()), getLines().size() == 1 ? 8 : getLines().size() * 10);
    }

    /**
     * See {@link net.minecraft.client.gui.inventory.GuiContainer#drawHoveringText}
     */
    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        GlStateManager.disableDepth();

        if (!getLines().isEmpty()) {
            int x = getX();
            int y = getY();
            int width = getWidth();
            int height = getHeight();

            final int outlineColor = 0xf0100010;
            drawRect(x - 3, y - 4, x + width + 3, y - 3, outlineColor);
            drawRect(x - 3, y + height + 3, x + width + 3, y + height + 4, outlineColor);
            drawRect(x - 3, y - 3, x + width + 3, y + height + 3, outlineColor);
            drawRect(x - 4, y - 3, x - 3, y + height + 3, outlineColor);
            drawRect(x + width + 3, y - 3, x + width + 4, y + height + 3, outlineColor);

            int gradient1 = 1347420415;
            int gradient2 = (gradient1 & 16711422) >> 1 | gradient1 & -16777216;
            drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, gradient1, gradient2);
            drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, gradient1, gradient2);
            drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, gradient1, gradient1);
            drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, gradient2, gradient2);

            for (int index = 0; index < getLines().size(); ++index) {
                font.drawStringWithShadow(getLines().get(index), x, y + index * 10, -1);
            }
        }

        GlStateManager.enableDepth();
    }

}

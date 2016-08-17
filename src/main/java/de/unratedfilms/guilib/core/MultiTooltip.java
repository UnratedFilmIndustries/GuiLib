
package de.unratedfilms.guilib.core;

import java.util.List;
import net.minecraft.client.Minecraft;

public class MultiTooltip extends Widget {

    protected int              color, txtColor;
    private final List<String> text;

    public MultiTooltip(List<String> strings) {

        super(getMaxStringWidth(strings) + 4, strings.size() * 12);

        text = strings;
        zLevel = 1.0f;
        color = 0xff000000;
        txtColor = 0xffffff;
    }

    public MultiTooltip(List<String> strings, int color, int txtColor) {

        super(getMaxStringWidth(strings) + 4, strings.size() * 12);

        text = strings;
        zLevel = 1.0f;
        this.color = color;
        this.txtColor = txtColor;
    }

    @Override
    public void draw(int mx, int my) {

        drawRect(x, y, x + width, y + height, color);

        int textY = y + 2;
        for (String line : text) {
            MC.fontRenderer.drawStringWithShadow(line, x + 2, textY, txtColor);
            textY += 11;
        }
    }

    @Override
    public boolean click(int mx, int my, MouseButton mouseButton) {

        return false;
    }

    public void setBackgroundColor(int color) {

        this.color = color;
    }

    public void setTextColor(int color) {

        txtColor = color;
    }

    public static int getMaxStringWidth(List<String> strings) {

        Minecraft mc = Minecraft.getMinecraft();
        int max = 0;
        for (String s : strings) {
            int width = mc.fontRenderer.getStringWidth(s);
            if (width > max) {
                max = width;
            }
        }
        return max;
    }

}

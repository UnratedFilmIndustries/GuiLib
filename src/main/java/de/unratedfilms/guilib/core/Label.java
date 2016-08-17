
package de.unratedfilms.guilib.core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public class Label extends Widget {

    private String             label;
    private int                color, hoverColor;

    private final List<Widget> tooltips;
    private boolean            hover;
    private final boolean      center;
    private boolean            shadow;
    private long               hoverStart;

    public Label(String label, int color, int hoverColor, Widget... tooltips) {

        this(label, color, hoverColor, true, tooltips);
    }

    public Label(String label, Widget... tooltips) {

        this(label, 0xffffff, 0xffffff, true, tooltips);
    }

    public Label(String label, int color, int hoverColor, boolean center, Widget... tooltips) {

        super(getStringWidth(label), 11);

        this.center = center;
        this.label = label;
        this.color = color;
        this.hoverColor = hoverColor;
        shadow = true;
        this.tooltips = new ArrayList<>();
        for (Widget w : tooltips) {
            this.tooltips.add(w);
        }
    }

    public Label(String label, boolean center, Widget... tooltips) {

        this(label, 0xffffff, 0xffffff, center, tooltips);
    }

    public void setColor(int color) {

        this.color = color;
    }

    public void setHoverColor(int hoverColor) {

        this.hoverColor = hoverColor;
    }

    public void setShadowedText(boolean useShadow) {

        shadow = useShadow;
    }

    public String getLabel() {

        return label;
    }

    public void setLabel(String label) {

        // Find the center
        if (center) {
            x += width / 2;
        }
        this.label = label;
        width = getStringWidth(label);
        if (center) {
            x -= width / 2;
        }
    }

    @Override
    public void draw(int mx, int my) {

        boolean newHover = inBounds(mx, my);

        if (newHover && !hover) {
            hoverStart = System.currentTimeMillis();
            // Label is designed for a single tooltip
            for (Widget w : tooltips) {
                w.setPosition(mx + 3, y + height);
            }
        }
        hover = newHover;

        if (shadow) {
            MC.fontRenderer.drawStringWithShadow(label, x, y + 2, hover ? hoverColor : color);
        } else {
            MC.fontRenderer.drawString(label, x, y + 2, hover ? hoverColor : color);
        }
    }

    @Override
    public List<Widget> getTooltips() {

        return hover && System.currentTimeMillis() - hoverStart >= 500 ? tooltips : super.getTooltips();
    }

    @Override
    public boolean click(int mx, int my, MouseButton mouseButton) {

        return false;
    }

    private static int getStringWidth(String text) {

        return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

    @Override
    public void setPosition(int x, int y) {

        this.x = center ? x - width / 2 : x;
        this.y = y;
    }

}

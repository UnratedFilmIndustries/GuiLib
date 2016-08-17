
package de.unratedfilms.guilib.focusable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Scrollbar.Shiftable;
import de.unratedfilms.guilib.core.Widget;

/**
 * A simple focusable label.
 */
public class FocusableLabel extends FocusableWidget implements Shiftable {

    private String             label;
    private int                color, hoverColor, focusColor;
    private final List<Widget> tooltips;
    private boolean            hover;
    private final boolean      center;
    private boolean            focused;
    private Object             userData;

    public FocusableLabel(String label, int color, int hoverColor, int focusColor, Widget... tooltips) {

        this(label, color, hoverColor, focusColor, true, tooltips);
    }

    public FocusableLabel(String label, Widget... tooltips) {

        this(label, 0xffffff, 16777120, 0x22aaff, true, tooltips);
    }

    public FocusableLabel(String label, int color, int hoverColor, int focusColor, boolean center, Widget... tooltips) {

        super(getStringWidth(label), 11);

        this.center = center;
        this.label = label;
        this.color = color;
        this.hoverColor = hoverColor;
        this.focusColor = focusColor;
        this.tooltips = new ArrayList<>();
        for (Widget w : tooltips) {
            this.tooltips.add(w);
        }
    }

    public FocusableLabel(String label, boolean center, Widget... tooltips) {

        this(label, 0xffffff, 16777120, 0x22aaff, center, tooltips);
    }

    public FocusableLabel(int x, int y, String label, Widget... tooltips) {

        this(label, 0xffffff, 16777120, 0x22aaff, true, tooltips);

        setPosition(x, y);
    }

    public void setColors(int color, int hoverColor, int focusColor) {

        this.color = color;
        this.hoverColor = hoverColor;
        this.focusColor = focusColor;
    }

    public void setColor(int color) {

        this.color = color;
    }

    public void setHoverColor(int hoverColor) {

        this.hoverColor = hoverColor;
    }

    public void setFocusColor(int focusColor) {

        this.focusColor = focusColor;
    }

    public String getLabel() {

        return label;
    }

    public void setLabel(String label) {

        if (center) {
            x += width / 2;
        }
        this.label = label;
        width = getStringWidth(label);
        if (center) {
            x -= width / 2;
        }
    }

    public void setUserData(Object data) {

        userData = data;
    }

    public Object getUserData() {

        return userData;
    }

    @Override
    public void draw(int mx, int my) {

        boolean newHover = inBounds(mx, my);
        if (newHover && !hover) {
            for (Widget w : tooltips) {
                w.setPosition(mx + 3, y + height);
            }
        }
        hover = newHover;
        if (focused) {
            drawRect(x, y, x + width, y + height, 0x99999999);
        }
        MC.fontRenderer.drawStringWithShadow(label, x, y + 2, focused ? focusColor : hover ? hoverColor : color);
    }

    @Override
    public List<Widget> getTooltips() {

        return hover ? tooltips : super.getTooltips();
    }

    @Override
    public boolean click(int mx, int my, MouseButton mouseButton) {

        return mouseButton == MouseButton.LEFT && inBounds(mx, my);
    }

    private static int getStringWidth(String text) {

        return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

    @Override
    public void setPosition(int x, int y) {

        this.x = center ? x - width / 2 : x;
        this.y = y;
    }

    @Override
    public void shiftY(int dy) {

        y += dy;
    }

    @Override
    public void focusGained() {

        focused = true;
    }

    @Override
    public void focusLost() {

        focused = false;
    }

}

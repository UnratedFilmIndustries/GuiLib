
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.widgets.view.adapters.LabelAdapter;

public class LabelImpl extends LabelAdapter {

    private int     color, hoverColor;
    private boolean shadowed;

    public LabelImpl(String text) {

        this(text, 0xffffff, 0xffffff);
    }

    public LabelImpl(String text, int color, int hoverColor) {

        // Note that the width will be adjusted by the setText() method
        super(0, 11, text);

        setText(text);
        this.color = color;
        this.hoverColor = hoverColor;
        shadowed = true;
    }

    @Override
    public void setText(String text) {

        super.setText(text);

        setWidth(MC.fontRenderer.getStringWidth(text));
    }

    public int getColor() {

        return color;
    }

    public void setColor(int color) {

        this.color = color;
    }

    public int getHoverColor() {

        return hoverColor;
    }

    public void setHoverColor(int hoverColor) {

        this.hoverColor = hoverColor;
    }

    public boolean isShadowed() {

        return shadowed;
    }

    public void setShadowed(boolean shadowed) {

        this.shadowed = shadowed;
    }

    @Override
    public void draw(int mx, int my) {

        boolean hover = inBounds(mx, my);
        int finalColor = hover ? hoverColor : color;
        if (shadowed) {
            MC.fontRenderer.drawStringWithShadow(getText(), getX(), getY() + 2, finalColor);
        } else {
            MC.fontRenderer.drawString(getText(), getX(), getY() + 2, finalColor);
        }
    }

}


package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.widgets.view.adapters.LabelAdapter;

public class LabelImpl extends LabelAdapter {

    private int     color, hoverColor;
    private boolean shadowed;

    public LabelImpl(String text) {

        this(text, 0xffffff, 0xffffff);
    }

    public LabelImpl(String text, int color, int hoverColor) {

        super(text);

        setText(text);
        this.color = color;
        this.hoverColor = hoverColor;
        shadowed = true;
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
    protected void doRevalidate() {

        setSize(MC.fontRenderer.getStringWidth(getText()), 11);
    }

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        boolean hover = inLocalBounds(viewport, lmx, lmy);
        int finalColor = hover ? hoverColor : color;
        if (shadowed) {
            MC.fontRenderer.drawStringWithShadow(getText(), getX(), getY() + 2, finalColor);
        } else {
            MC.fontRenderer.drawString(getText(), getX(), getY() + 2, finalColor);
        }
    }

}

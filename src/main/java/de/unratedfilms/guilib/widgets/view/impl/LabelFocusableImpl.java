
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.widgets.model.LabelFocusable;

/**
 * A {@link LabelImpl} that can be focused. It's quite useful for lists where the user selects one option by clicking on it (so it gains focus).
 */
public class LabelFocusableImpl extends LabelImpl implements LabelFocusable {

    private Object  userData;
    private int     focusColor;

    private boolean focused;

    public LabelFocusableImpl(String text) {

        this(text, 0xffffff, 16777120, 0x22aaff);
    }

    public LabelFocusableImpl(String text, int color, int hoverColor, int focusColor) {

        super(text, color, hoverColor);

        this.focusColor = focusColor;
    }

    @Override
    public Object getUserData() {

        return userData;
    }

    @Override
    public void setUserData(Object userData) {

        this.userData = userData;
    }

    public int getFocusColor() {

        return focusColor;
    }

    public void setFocusColor(int focusColor) {

        this.focusColor = focusColor;
    }

    @Override
    public boolean isFocused() {

        return focused;
    }

    @Override
    public void draw(int mx, int my) {

        if (focused) {
            drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), focusColor);
        }

        boolean hover = inBounds(mx, my);
        int finalColor = focused ? focusColor : hover ? getHoverColor() : getColor();
        if (isShadowed()) {
            MC.fontRenderer.drawStringWithShadow(getText(), getX(), getY() + 2, finalColor);
        } else {
            MC.fontRenderer.drawString(getText(), getX(), getY() + 2, finalColor);
        }
    }

    @Override
    public boolean mousePressed(int mx, int my, MouseButton mouseButton) {

        return mouseButton == MouseButton.LEFT && inBounds(mx, my);
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


package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.widgets.view.adapters.TextFieldAdapter;

/**
 * Vanilla GuiTextField in Widget form.
 */
public class TextFieldImpl extends TextFieldAdapter {

    private int innerColor, outerColor;

    public TextFieldImpl(CharacterFilter filter) {

        this(200, 20, filter);
    }

    public TextFieldImpl(int width, int height, CharacterFilter filter) {

        this(width, height, -16777216, -6250336, filter);
    }

    public TextFieldImpl(int width, int height, int innerColor, int outerColor, CharacterFilter filter) {

        super(width, height, filter);

        this.innerColor = innerColor;
        this.outerColor = outerColor;
    }

    public int getInnerColor() {

        return innerColor;
    }

    public void setInnerColor(int innerColor) {

        this.innerColor = innerColor;
    }

    public int getOuterColor() {

        return outerColor;
    }

    public void setOuterColor(int outerColor) {

        this.outerColor = outerColor;
    }

    @Override
    protected int getDrawX() {

        return getX() + 4;
    }

    @Override
    protected int getDrawY() {

        return getY() + (getHeight() - 8) / 2;
    }

    @Override
    public int getInternalWidth() {

        return getWidth() - 8;
    }

    @Override
    protected void drawBackground() {

        drawRect(getX() - 1, getY() - 1, getX() + getWidth() + 1, getY() + getHeight() + 1, outerColor);
        drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), innerColor);
    }

}

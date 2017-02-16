
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.widgets.view.adapters.TextFieldAdapter;

/**
 * Vanilla GuiTextField in Widget form.
 */
public class TextFieldImpl extends TextFieldAdapter {

    private int innerColor, outerColor;

    public TextFieldImpl() {

        this(new VanillaFilter());
    }

    public TextFieldImpl(CharacterFilter filter) {

        this(-16777216, -6250336, filter);
    }

    public TextFieldImpl(int innerColor, int outerColor) {

        this(innerColor, outerColor, new VanillaFilter());
    }

    public TextFieldImpl(int innerColor, int outerColor, CharacterFilter filter) {

        super(filter);

        setSize(200, 16);

        this.innerColor = innerColor;
        this.outerColor = outerColor;
    }

    public int getInnerColor() {

        return innerColor;
    }

    public TextFieldImpl setInnerColor(int innerColor) {

        this.innerColor = innerColor;
        return this;
    }

    public int getOuterColor() {

        return outerColor;
    }

    public TextFieldImpl setOuterColor(int outerColor) {

        this.outerColor = outerColor;
        return this;
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

        drawRect(getX(), getY(), getRight(), getBottom(), outerColor);
        drawRect(getX() + 1, getY() + 1, getRight() - 1, getBottom() - 1, innerColor);
    }

}

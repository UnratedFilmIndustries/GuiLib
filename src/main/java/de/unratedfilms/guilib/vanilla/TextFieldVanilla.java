
package de.unratedfilms.guilib.vanilla;

import de.unratedfilms.guilib.core.TextField;

/**
 * Vanilla GuiTextField in Widget form.
 */
public class TextFieldVanilla extends TextField {

    private int outerColor, innerColor;

    public TextFieldVanilla(int width, int height, CharacterFilter filter) {

        super(width, height, filter);

        outerColor = -6250336;
        innerColor = -16777216;
    }

    public TextFieldVanilla(CharacterFilter filter) {

        this(200, 20, filter);
    }

    public TextFieldVanilla(int width, int height, int innerColor, int outerColor, CharacterFilter filter) {

        super(width, height, filter);

        this.outerColor = outerColor;
        this.innerColor = innerColor;
    }

    public void setInnerColor(int c) {

        innerColor = c;
    }

    public void setOuterColor(int c) {

        outerColor = c;
    }

    @Override
    protected int getDrawX() {

        return x + 4;
    }

    @Override
    protected int getDrawY() {

        return y + (height - 8) / 2;
    }

    @Override
    public int getInternalWidth() {

        return width - 8;
    }

    @Override
    protected void drawBackground() {

        drawRect(x - 1, y - 1, x + width + 1, y + height + 1, outerColor);
        drawRect(x, y, x + width, y + height, innerColor);
    }

}

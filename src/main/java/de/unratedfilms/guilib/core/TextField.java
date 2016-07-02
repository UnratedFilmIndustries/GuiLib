
package de.unratedfilms.guilib.core;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import de.unratedfilms.guilib.focusable.FocusableWidget;

/**
 * Abstract representation of a text field.
 * This is pretty much a copy of the vanilla text field, so it support highlighting, copying, and pasting text.
 */
public abstract class TextField extends FocusableWidget {

    private int             maxLength = 32;
    private CharacterFilter filter;
    private int             color     = 0xffffff;

    private String          text      = "";

    protected boolean       focused;
    protected int           cursorCounter, cursorPosition, charOffset, selectionEnd;

    public TextField(int width, int height) {

        this(width, height, new VanillaFilter());
    }

    public TextField(int width, int height, CharacterFilter filter) {

        super(width, height);

        this.filter = filter;
    }

    public int getMaxLength() {

        return maxLength;
    }

    public void setMaxLength(int length) {

        maxLength = length;

        if (text.length() > length) {
            text = text.substring(0, length);
        }
    }

    public CharacterFilter getFilter() {

        return filter;
    }

    public void setFilter(CharacterFilter filter) {

        this.filter = filter;
    }

    public int getColor() {

        return color;
    }

    public void setColor(int color) {

        this.color = color;
    }

    public String getText() {

        return text;
    }

    public String getSelectedText() {

        int start = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
        int end = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
        return text.substring(start, end);
    }

    public void setText(String text) {

        this.text = text.length() > maxLength ? text.substring(0, maxLength) : text;
        setCursorPosition(this.text.length());
    }

    public void insertTextAtCursor(String str) {

        String s1 = "";
        str = applyCharacterFilter(str);
        int i = cursorPosition < selectionEnd ? cursorPosition
                : selectionEnd;
        int j = cursorPosition < selectionEnd ? selectionEnd
                : cursorPosition;
        int k = maxLength - text.length() - (i - selectionEnd);

        if (text.length() > 0) {
            s1 = s1 + text.substring(0, i);
        }

        int l;

        if (k < str.length()) {
            s1 = s1 + str.substring(0, k);
            l = k;
        } else {
            s1 = s1 + str;
            l = str.length();
        }

        if (text.length() > 0 && j < text.length()) {
            s1 = s1 + text.substring(j);
        }

        text = s1;
        moveCursorBy(i - selectionEnd + l);
    }

    private String applyCharacterFilter(String str) {

        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (filter.isAllowedCharacter(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    public void deleteFromCursor(int amt) {

        if (text.length() > 0) {
            if (selectionEnd != cursorPosition) {
                insertTextAtCursor("");
            } else {
                boolean flag = amt < 0;
                int j = flag ? cursorPosition + amt : cursorPosition;
                int k = flag ? cursorPosition : cursorPosition + amt;
                String s = "";
                if (j >= 0) {
                    s = text.substring(0, j);
                }
                if (k < text.length()) {
                    s = s + text.substring(k);
                }
                text = s;
                if (flag) {
                    moveCursorBy(amt);
                }
            }
        }
    }

    public void setCursorPosition(int index) {

        cursorPosition = MathHelper.clamp_int(index, 0, text.length());
        setSelectionPos(cursorPosition);
    }

    public void moveCursorBy(int offs) {

        setCursorPosition(selectionEnd + offs);
    }

    public void setSelectionPos(int index) {

        index = MathHelper.clamp_int(index, 0, text.length());
        selectionEnd = index;

        if (charOffset > index) {
            charOffset = index;
        }

        int width = getInternalWidth();
        String s = MC.fontRenderer.trimStringToWidth(text.substring(charOffset), width);
        int pos = s.length() + charOffset;

        if (index == charOffset) {
            charOffset -= MC.fontRenderer.trimStringToWidth(text, width, true).length();
        }
        if (index > pos) {
            charOffset += index - 1;
        } else if (index <= charOffset) {
            charOffset = index;
        }

        charOffset = MathHelper.clamp_int(charOffset, 0, text.length());
    }

    protected abstract int getDrawX();

    protected abstract int getDrawY();

    public abstract int getInternalWidth();

    protected abstract void drawBackground();

    @Override
    public void draw(int mx, int my) {

        drawBackground();

        int j = cursorPosition - charOffset;
        int k = selectionEnd - charOffset;
        String s = MC.fontRenderer.trimStringToWidth(text.substring(charOffset), getInternalWidth());
        boolean flag = j >= 0 && j <= s.length();
        boolean cursor = focused && cursorCounter / 6 % 2 == 0 && flag;
        int l = getDrawX();
        int i1 = getDrawY();
        int j1 = l;

        if (k > s.length()) {
            k = s.length();
        }

        if (s.length() > 0) {
            String s1 = flag ? s.substring(0, j) : s;
            j1 = MC.fontRenderer.drawStringWithShadow(s1, l, i1, color);
        }

        boolean flag2 = cursorPosition < text.length() || text.length() >= maxLength;
        int k1 = j1;

        if (!flag) {
            k1 = j > 0 ? l + width : l;
        } else if (flag2) {
            k1 = j1 - 1;
            --j1;
        }
        if (s.length() > 0 && flag && j < s.length()) {
            MC.fontRenderer.drawStringWithShadow(s.substring(j), j1, i1, color);
        }
        if (cursor) {
            if (flag2) {
                Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + MC.fontRenderer.FONT_HEIGHT, -3092272);
            } else {
                MC.fontRenderer.drawStringWithShadow("_", k1, i1, color);
            }
        }
        if (k != j) {
            int l1 = l + MC.fontRenderer.getStringWidth(s.substring(0, k));
            drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + MC.fontRenderer.FONT_HEIGHT);
        }

    }

    protected void drawCursorVertical(int x1, int y1, int x2, int y2) {

        int temp;
        if (x1 < x2) {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 < y2) {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_REVERSE);
        tessellator.startDrawingQuads();
        tessellator.addVertex(x1, y2, 0.0D);
        tessellator.addVertex(x2, y2, 0.0D);
        tessellator.addVertex(x2, y1, 0.0D);
        tessellator.addVertex(x1, y1, 0.0D);
        tessellator.draw();
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public boolean click(int mx, int my) {

        return inBounds(mx, my);
    }

    @Override
    public void handleClick(int mx, int my) {

        int pos = mx - x;
        pos -= Math.abs(getInternalWidth() - width) / 2;

        String s = MC.fontRenderer.trimStringToWidth(
                text.substring(charOffset), getWidth());
        setCursorPosition(MC.fontRenderer.trimStringToWidth(s, pos).length()
                + charOffset);
    }

    @Override
    public void update() {

        ++cursorCounter;
    }

    @Override
    public void focusGained() {

        cursorCounter = 0;
        focused = true;
    }

    @Override
    public void focusLost() {

        focused = false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {

        if (focused) {
            switch (typedChar) {
                case 1:
                    setCursorPosition(text.length());
                    setSelectionPos(0);
                    return true;
                case 3:
                    GuiScreen.setClipboardString(getSelectedText());
                    return true;
                case 22:
                    insertTextAtCursor(GuiScreen.getClipboardString());
                    return true;
                case 24:
                    GuiScreen.setClipboardString(getSelectedText());
                    insertTextAtCursor("");
                    return true;
                default:
                    switch (keyCode) {
                        case 14:
                            deleteFromCursor(-1);
                            return true;
                        case 199:
                            setSelectionPos(0);
                            setCursorPosition(0);
                            return true;
                        case 203:
                            if (GuiScreen.isShiftKeyDown()) {
                                setSelectionPos(selectionEnd - 1);
                            } else {
                                moveCursorBy(-1);
                            }
                            return true;
                        case 205:
                            if (GuiScreen.isShiftKeyDown()) {
                                setSelectionPos(selectionEnd + 1);
                            } else {
                                moveCursorBy(1);
                            }
                            return true;
                        case 207:
                            if (GuiScreen.isShiftKeyDown()) {
                                setSelectionPos(text.length());
                            } else {
                                setCursorPosition(text.length());
                            }
                            return true;
                        case 211:
                            deleteFromCursor(1);
                            return true;
                        default:
                            if (filter.isAllowedCharacter(typedChar)) {
                                insertTextAtCursor(Character.toString(typedChar));
                                return true;
                            }
                            return false;
                    }
            }
        }
        return false;
    }

    public interface CharacterFilter {

        public boolean isAllowedCharacter(char c);

    }

    /*
     * Default character filters
     */

    public static class VanillaFilter implements CharacterFilter {

        @Override
        public boolean isAllowedCharacter(char c) {

            return ChatAllowedCharacters.isAllowedCharacter(c);
        }

    }

    public static class IntegerNumberFilter implements CharacterFilter {

        @Override
        public boolean isAllowedCharacter(char c) {

            return Character.isDigit(c) || c == '-';
        }

    }

    public static class DecimalNumberFilter implements CharacterFilter {

        @Override
        public boolean isAllowedCharacter(char c) {

            return Character.isDigit(c) || c == '-' || c == '.';
        }

    }

}
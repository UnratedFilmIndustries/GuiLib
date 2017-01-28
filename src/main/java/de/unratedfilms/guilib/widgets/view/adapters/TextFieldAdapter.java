
package de.unratedfilms.guilib.widgets.view.adapters;

import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.TextField;

/**
 * A minimal implementation of {@link TextField} that only contains drawing code for painting the text and the cursor.
 * The border and background of the text field must be painted by a subclass.
 */
public abstract class TextFieldAdapter extends ContextHelperWidgetAdapter implements TextField, WidgetFocusable {

    private TextFieldHandler handler;

    private int              maxLength = 32;
    private CharacterFilter  filter;
    private int              textColor = 0xffffff;

    private String           text      = "";

    private boolean          focused;
    protected int            cursorCounter, cursorPosition, charOffset, selectionEnd;

    public TextFieldAdapter() {

        this(new VanillaFilter());
    }

    public TextFieldAdapter(CharacterFilter filter) {

        this.filter = filter;
    }

    @Override
    public TextFieldHandler getHandler() {

        return handler;
    }

    @Override
    public TextFieldAdapter setHandler(TextFieldHandler handler) {

        this.handler = handler;
        return this;
    }

    @Override
    public int getMaxLength() {

        return maxLength;
    }

    @Override
    public TextFieldAdapter setMaxLength(int length) {

        maxLength = length;

        if (text.length() > length) {
            text = text.substring(0, length);
        }

        return this;
    }

    @Override
    public CharacterFilter getFilter() {

        return filter;
    }

    @Override
    public TextFieldAdapter setFilter(CharacterFilter filter) {

        this.filter = filter;
        return this;
    }

    public int getTextColor() {

        return textColor;
    }

    public void setTextColor(int textColor) {

        this.textColor = textColor;
    }

    @Override
    public String getText() {

        return text;
    }

    @Override
    public TextFieldAdapter setText(String text) {

        Validate.notNull(text, "Text field text cannot be null");
        this.text = text.length() > maxLength ? text.substring(0, maxLength) : text;
        setCursorPosition(this.text.length());

        return this;
    }

    @Override
    public boolean isFocused() {

        return focused;
    }

    @Override
    public String getSelectedText() {

        int start = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
        int end = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
        return text.substring(start, end);
    }

    @Override
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

    @Override
    public void deleteTextFromCursor(int offset) {

        if (text.length() > 0) {
            if (selectionEnd != cursorPosition) {
                insertTextAtCursor("");
            } else {
                boolean flag = offset < 0;
                int j = flag ? cursorPosition + offset : cursorPosition;
                int k = flag ? cursorPosition : cursorPosition + offset;
                String s = "";
                if (j >= 0) {
                    s = text.substring(0, j);
                }
                if (k < text.length()) {
                    s = s + text.substring(k);
                }
                text = s;
                if (flag) {
                    moveCursorBy(offset);
                }
            }
        }
    }

    @Override
    public void setCursorPosition(int index) {

        cursorPosition = MathHelper.clamp(index, 0, text.length());
        setSelectionPosition(cursorPosition);
    }

    @Override
    public void moveCursorBy(int offset) {

        setCursorPosition(selectionEnd + offset);
    }

    @Override
    public void setSelectionPosition(int index) {

        index = MathHelper.clamp(index, 0, text.length());
        selectionEnd = index;

        if (charOffset > index) {
            charOffset = index;
        }

        int width = getInternalWidth();
        String s = MC.fontRendererObj.trimStringToWidth(text.substring(charOffset), width);
        int pos = s.length() + charOffset;

        if (index == charOffset) {
            charOffset -= MC.fontRendererObj.trimStringToWidth(text, width, true).length();
        }
        if (index > pos) {
            charOffset += index - 1;
        } else if (index <= charOffset) {
            charOffset = index;
        }

        charOffset = MathHelper.clamp(charOffset, 0, text.length());
    }

    protected abstract int getDrawX();

    protected abstract int getDrawY();

    public abstract int getInternalWidth();

    protected abstract void drawBackground();

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        drawBackground();

        int j = cursorPosition - charOffset;
        int k = selectionEnd - charOffset;
        String s = MC.fontRendererObj.trimStringToWidth(text.substring(charOffset), getInternalWidth());
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
            j1 = MC.fontRendererObj.drawStringWithShadow(s1, l, i1, textColor);
        }

        boolean flag2 = cursorPosition < text.length() || text.length() >= maxLength;
        int k1 = j1;

        if (!flag) {
            k1 = j > 0 ? l + getWidth() : l;
        } else if (flag2) {
            k1 = j1 - 1;
            --j1;
        }
        if (s.length() > 0 && flag && j < s.length()) {
            MC.fontRendererObj.drawStringWithShadow(s.substring(j), j1, i1, textColor);
        }
        if (cursor) {
            if (flag2) {
                Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + MC.fontRendererObj.FONT_HEIGHT, -3092272);
            } else {
                MC.fontRendererObj.drawStringWithShadow("_", k1, i1, textColor);
            }
        }
        if (k != j) {
            int l1 = l + MC.fontRendererObj.getStringWidth(s.substring(0, k));
            drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + MC.fontRendererObj.FONT_HEIGHT);
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

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();

        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);

        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(x1, y2, 0.0D).endVertex();
        vertexbuffer.pos(x2, y2, 0.0D).endVertex();
        vertexbuffer.pos(x2, y1, 0.0D).endVertex();
        vertexbuffer.pos(x1, y1, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    @Override
    public void update() {

        cursorCounter++;
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
    public boolean mousePressedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT && inLocalBounds(viewport, lmx, lmy)) {
            int pos = lmx - getX();
            pos -= Math.abs(getInternalWidth() - getWidth()) / 2;

            String s = MC.fontRendererObj.trimStringToWidth(
                    text.substring(charOffset), getWidth());
            setCursorPosition(MC.fontRendererObj.trimStringToWidth(s, pos).length()
                    + charOffset);

            return true;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {

        if (!focused) {
            return false;
        }

        boolean result = handleKeyType(typedChar, keyCode);

        if (handler != null) {
            handler.keyTyped(this, typedChar, keyCode);
        }

        return result;
    }

    private boolean handleKeyType(char typedChar, int keyCode) {

        switch (typedChar) {
            case 1:
                setCursorPosition(text.length());
                setSelectionPosition(0);
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
                        deleteTextFromCursor(-1);
                        return true;
                    case 199:
                        setSelectionPosition(0);
                        setCursorPosition(0);
                        return true;
                    case 203:
                        if (GuiScreen.isShiftKeyDown()) {
                            setSelectionPosition(selectionEnd - 1);
                        } else {
                            moveCursorBy(-1);
                        }
                        return true;
                    case 205:
                        if (GuiScreen.isShiftKeyDown()) {
                            setSelectionPosition(selectionEnd + 1);
                        } else {
                            moveCursorBy(1);
                        }
                        return true;
                    case 207:
                        if (GuiScreen.isShiftKeyDown()) {
                            setSelectionPosition(text.length());
                        } else {
                            setCursorPosition(text.length());
                        }
                        return true;
                    case 211:
                        deleteTextFromCursor(1);
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

}


package de.unratedfilms.guilib.widgets.model;

import net.minecraft.util.ChatAllowedCharacters;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.core.WidgetFocusable;

/**
 * Abstract representation of a text field.
 * This is pretty much a copy of the vanilla text field, so it should support highlighting, copying, and pasting text.
 */
public interface TextField extends WidgetFlexible, WidgetFocusable {

    public int getMaxLength();

    public TextField setMaxLength(int length);

    public CharacterFilter getFilter();

    public TextField setFilter(CharacterFilter filter);

    public String getText();

    public TextField setText(String text);

    public String getSelectedText();

    public void insertTextAtCursor(String text);

    public void deleteTextFromCursor(int offset);

    public void setCursorPosition(int index);

    public void moveCursorBy(int offset);

    public void setSelectionPosition(int index);

    public static interface CharacterFilter {

        public boolean isAllowedCharacter(char c);

    }

    /*
     * Preset character filters
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

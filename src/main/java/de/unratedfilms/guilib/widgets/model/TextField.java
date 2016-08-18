
package de.unratedfilms.guilib.widgets.model;

import net.minecraft.util.ChatAllowedCharacters;
import de.unratedfilms.guilib.core.FocusableWidget;
import de.unratedfilms.guilib.core.Widget;

/**
 * Abstract representation of a text field.
 * This is pretty much a copy of the vanilla text field, so it should support highlighting, copying, and pasting text.
 */
public interface TextField extends Widget, FocusableWidget {

    public int getMaxLength();

    public void setMaxLength(int length);

    public CharacterFilter getFilter();

    public void setFilter(CharacterFilter filter);

    public String getText();

    public void setText(String text);

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

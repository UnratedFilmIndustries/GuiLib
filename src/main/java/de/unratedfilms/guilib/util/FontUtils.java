
package de.unratedfilms.guilib.util;

import java.util.List;
import net.minecraft.client.gui.FontRenderer;

public class FontUtils {

    public static int getMaxStringWidth(FontRenderer font, List<String> strings) {

        int max = 0;
        for (String s : strings) {
            int width = font.getStringWidth(s);
            if (width > max) {
                max = width;
            }
        }
        return max;
    }

    public static String abbreviateIfTooLong(FontRenderer font, String string, int maxWidth) {

        int stringWidth = font.getStringWidth(string);
        int ellipsisWidth = font.getStringWidth("...");

        if (stringWidth > maxWidth && stringWidth > ellipsisWidth) {
            return font.trimStringToWidth(string, maxWidth - ellipsisWidth).trim() + "...";
        } else {
            return string;
        }
    }

    private FontUtils() {}

}

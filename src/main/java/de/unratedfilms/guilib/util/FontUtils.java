
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

    private FontUtils() {}

}

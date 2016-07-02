
package de.unratedfilms.guilib.vanilla;

import de.unratedfilms.guilib.core.Scrollbar;
import de.unratedfilms.guilib.core.Widget;

/**
 * Default style scrollbar.
 */
public class ScrollbarVanilla extends Scrollbar {

    public ScrollbarVanilla(int width) {

        super(width);
    }

    @Override
    protected void drawBoundary(int x, int y, int width, int height) {

        drawRect(x, y, x + width, y + height, 0x80000000);
    }

    @Override
    protected void drawScrollbar(int x, int y, int width, int height) {

        drawGradientRect(x, y, x + width, y + height, 0x80ffffff, 0x80777777);
    }

    @Override
    protected void shiftChildren(int dy) {

        for (Widget w : getContainer().getWidgets()) {
            if (w instanceof Shiftable) {
                ((Shiftable) w).shiftY(dy);
            }
        }
    }

}

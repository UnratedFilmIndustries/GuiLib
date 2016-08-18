
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.widgets.model.Scrollbar;
import de.unratedfilms.guilib.widgets.view.adapters.ScrollbarAdapter;

/**
 * Default style {@link Scrollbar}.
 */
public class ScrollbarImpl extends ScrollbarAdapter {

    public ScrollbarImpl(int width) {

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

}

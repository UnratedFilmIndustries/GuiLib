
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.Scrollbar;
import de.unratedfilms.guilib.widgets.view.adapters.ScrollbarAdapter;

/**
 * Default style {@link Scrollbar}.
 */
public class ScrollbarImpl extends ScrollbarAdapter {

    /**
     * Creates a new scrollbar with the default width.
     *
     * @param extraScrollHeight The sum of the supposed gap between the top+bottom of the container and the {@link Widget}s that are inside.
     *        Say that the items in the list should go from the container's top+2 to the container's bottom-2, then extraScrollHeight should be 4.
     */
    public ScrollbarImpl(int extraScrollHeight) {

        super(extraScrollHeight);
    }

    /**
     * Creates a new scrollbar.
     *
     * @param width The width of the widget in pixels.
     * @param extraScrollHeight The sum of the supposed gap between the top+bottom of the container and the {@link Widget}s that are inside.
     *        Say that the items in the list should go from the container's top+2 to the container's bottom-2, then extraScrollHeight should be 4.
     */
    public ScrollbarImpl(int width, int extraScrollHeight) {

        super(width, extraScrollHeight);
    }

    @Override
    protected void drawBoundaryInLocalContext() {

        drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80000000);
    }

    @Override
    protected void drawScrollbarInLocalContext(int y, int length) {

        drawGradientRect(getX(), y, getX() + getWidth(), y + length, 0x80ffffff, 0x80777777);
    }

}


package de.unratedfilms.guilib.widgets.view.adapters;

import net.minecraft.util.math.MathHelper;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Scrollbar;

/**
 * A minimal implementation of {@link Scrollbar} that doesn't contain any drawing code.
 */
public abstract class ScrollbarAdapter extends ContextHelperWidgetAdapter implements Scrollbar {

    private final int extraScrollHeight;
    private Container container;

    // The number of pixels all widgets inside the container are shifted upwards
    private int       widgetShift;

    private int       yClick = -1;

    /**
     * Creates a new scrollbar adapter with the default width.
     *
     * @param extraScrollHeight The sum of the supposed gap between the top+bottom of the container and the {@link Widget}s that are inside.
     *        Say that the items in the list should go from the container's top+2 to the container's bottom-2, then extraScrollHeight should be 4.
     */
    public ScrollbarAdapter(int extraScrollHeight) {

        this(10, extraScrollHeight);
    }

    /**
     * Creates a new scrollbar adapter.
     *
     * @param width The width of the widget in pixels.
     * @param extraScrollHeight The sum of the supposed gap between the top+bottom of the container and the {@link Widget}s that are inside.
     *        Say that the items in the list should go from the container's top+2 to the container's bottom-2, then extraScrollHeight should be 4.
     */
    public ScrollbarAdapter(int width, int extraScrollHeight) {

        setWidth(width);
        this.extraScrollHeight = extraScrollHeight;
    }

    @Override
    public Container getContainer() {

        return container;
    }

    @Override
    public void setContainer(Container container) {

        this.container = container;
    }

    protected int getContentHeight() {

        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (Widget w : container.getWidgets()) {
            if (w.getY() < minY) {
                minY = w.getY();
            }
            if (w.getY() + w.getHeight() > maxY) {
                maxY = w.getY() + w.getHeight();
            }
        }
        return minY > maxY ? 0 : maxY - minY + extraScrollHeight;
    }

    protected int getHeightDifference() {

        return getContentHeight() - getHeight();
    }

    @Override
    public int getWidgetShift() {

        return widgetShift;
    }

    @Override
    public void addWidgetShift(int p) {

        int heightDiff = getHeightDifference();

        if (heightDiff > 0) {
            widgetShift = MathHelper.clamp(widgetShift + p, 0, heightDiff);
        }
    }

    @Override
    public void addWidgetShiftRelative(int p) {

        int heightDiff = getHeightDifference();

        if (heightDiff > 0) {
            addWidgetShift((int) (p * (1 + heightDiff / (float) getHeight())));
        }
    }

    @Override
    protected void revalidateThis() {

        setHeight(container.getHeight());
    }

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        // If we don't actually need a scrollbar, don't draw one
        if (getHeightDifference() <= 0) {
            return;
        }

        // Make sure that the offset is updated if something in the container has changed.
        // That is done by just shifting by 0 pixels -- it probably won't change anything, but the shifting logic will make sure that the new offset is in fact valid.
        addWidgetShift(0);

        int length = getLength();

        if (yClick != -1) {
            float scrollMultiplier = 1.0F;
            int diff = getHeightDifference();

            if (diff < 1) {
                diff = 1;
            }

            scrollMultiplier /= (getHeight() - length) / (float) diff;
            addWidgetShift((int) ( (lmy - yClick) * scrollMultiplier));
            yClick = lmy;
        }

        drawBoundaryInLocalContext();

        int y = widgetShift * (getHeight() - length) / getHeightDifference() + getY();
        if (y < getY()) {
            y = getY();
        }

        drawScrollbarInLocalContext(y, length);
    }

    protected int getLength() {

        int contentHeight = getContentHeight();

        if (contentHeight == 0) {
            return 0;
        }
        int length = getHeight() * getHeight() / contentHeight;
        if (length < 32) {
            length = 32;
        }
        if (length > getHeight() - 8) {
            length = getHeight() - 8;
        }
        return length;
    }

    protected abstract void drawBoundaryInLocalContext();

    protected abstract void drawScrollbarInLocalContext(int y, int length);

    @Override
    public boolean mousePressedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT && inLocalBounds(viewport, lmx, lmy)) {
            yClick = lmy;
            return true;
        }

        return false;
    }

    @Override
    public void mouseReleasedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT) {
            yClick = -1;
        }
    }

}


package de.unratedfilms.guilib.widgets.view.adapters;

import org.lwjgl.input.Mouse;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetAdapter;
import de.unratedfilms.guilib.integration.Container;
import de.unratedfilms.guilib.widgets.model.Scrollbar;

/**
 * A minimal implementation of {@link Scrollbar} that doesn't contain any drawing code.
 */
public abstract class ScrollbarAdapter extends WidgetAdapter implements Scrollbar {

    private int       yClick;
    private Container container;

    private int       topY, bottomY;
    private int       offset;

    public ScrollbarAdapter(int width) {

        super(width, 0);

        yClick = -1;
    }

    @Override
    public Container getContainer() {

        return container;
    }

    @Override
    public void setContainer(Container container) {

        this.container = container;
    }

    @Override
    public void shift(int p) {

        int heightDiff = getHeightDifference();
        if (heightDiff > 0) {
            int dif = offset + p;
            if (dif > 0) {
                dif = 0;
            }
            if (dif < -heightDiff) {
                dif = -heightDiff;
            }
            int result = dif - offset;
            if (result != 0) {
                shiftChildren(result);
            }
            offset = dif;
        }
    }

    @Override
    public void shiftRelative(int p) {

        int heightDiff = getHeightDifference();
        if (heightDiff > 0) {
            p *= 1 + heightDiff / (float) (bottomY - topY);
            // shift(i) inlined
            int dif = offset + p;
            if (dif > 0) {
                dif = 0;
            }
            if (dif < -heightDiff) {
                dif = -heightDiff;
            }
            int result = dif - offset;
            if (result != 0) {
                shiftChildren(result);
            }
            offset = dif;
        }
    }

    private void shiftChildren(int dy) {

        for (Widget w : getContainer().getWidgets()) {
            w.setY(w.getY() + dy);
        }
    }

    @Override
    public void revalidate(int topY, int bottomY) {

        this.topY = topY;
        this.bottomY = bottomY;
        setHeight(bottomY - topY);
        int heightDiff = getHeightDifference();
        if (offset != 0 && heightDiff <= 0) {
            offset = 0;
        }
        if (heightDiff > 0 && offset < -heightDiff) {
            offset = -heightDiff;
        }
        if (offset != 0) {
            shiftChildren(offset);
        }
    }

    @Override
    public void onChildRemoved() {

        int heightDiff = getHeightDifference();
        if (offset != 0) {
            if (heightDiff <= 0) {
                shiftChildren(-offset);
                offset = 0;
            } else if (offset < -heightDiff) {
                shiftChildren(-heightDiff - offset);
                offset = -heightDiff;
            }
        }
    }

    protected int getHeightDifference() {

        return container.getContentHeight() - (bottomY - topY);
    }

    protected int getLength() {

        if (container.getContentHeight() == 0) {
            return 0;
        }
        int length = (bottomY - topY) * (bottomY - topY) / container.getContentHeight();
        if (length < 32) {
            length = 32;
        }
        if (length > bottomY - topY - 8) {
            length = bottomY - topY - 8;
        }
        return length;
    }

    @Override
    public void draw(int mx, int my) {

        int length = getLength();

        if (Mouse.isButtonDown(0)) {
            if (yClick == -1) {
                if (inBounds(mx, my)) {
                    yClick = my;
                }
            } else {
                float scrollMultiplier = 1.0F;
                int diff = getHeightDifference();

                if (diff < 1) {
                    diff = 1;
                }

                scrollMultiplier /= (bottomY - topY - length) / (float) diff;
                shift((int) ( (yClick - my) * scrollMultiplier));
                yClick = my;
            }
        } else {
            yClick = -1;
        }

        drawBoundary(getX(), topY, getWidth(), getHeight());

        int y = -offset * (bottomY - topY - length) / getHeightDifference() + topY;
        if (y < topY) {
            y = topY;
        }

        drawScrollbar(getX(), y, getWidth(), length);
    }

    protected abstract void drawBoundary(int x, int y, int width, int height);

    protected abstract void drawScrollbar(int x, int y, int width, int height);

    @Override
    public boolean shouldRender(int topY, int bottomY) {

        return getHeightDifference() > 0;
    }

}

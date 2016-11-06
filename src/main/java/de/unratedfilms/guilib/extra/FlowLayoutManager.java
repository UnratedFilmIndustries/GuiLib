
package de.unratedfilms.guilib.extra;

import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;

public class FlowLayoutManager implements LayoutManager {

    public static enum Axis {

        X, Y;

    }

    private final Container container;

    private final Axis      axis;
    private final int       paddingX, paddingY;
    private final int       gap;

    public FlowLayoutManager(Container container, Axis axis) {

        this(container, axis, 10, 10, 5);
    }

    public FlowLayoutManager(Container container, Axis axis, int paddingX, int paddingY, int gap) {

        this.container = container;
        this.axis = axis;
        this.paddingX = paddingX;
        this.paddingY = paddingY;
        this.gap = gap;
    }

    @Override
    public void layout() {

        if (axis == Axis.X) {
            int x = paddingX;
            for (Widget widget : container.getWidgets()) {
                widget.setPosition(x, paddingY);
                x += widget.getWidth() + gap;
            }
        } else if (axis == Axis.Y) {
            int y = paddingY;
            for (Widget widget : container.getWidgets()) {
                widget.setPosition(paddingX, y);
                y += widget.getHeight() + gap;
            }
        }
    }

}

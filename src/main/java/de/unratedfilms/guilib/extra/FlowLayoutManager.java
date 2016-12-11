
package de.unratedfilms.guilib.extra;

import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;

public class FlowLayoutManager implements LayoutManager {

    private final Container container;

    private final Axis      axis;
    private final int       padding, gap;

    public FlowLayoutManager(Container container, Axis axis) {

        this(container, axis, 10, 5);
    }

    public FlowLayoutManager(Container container, Axis axis, int padding, int gap) {

        this.container = container;
        this.axis = axis;
        this.padding = padding;
        this.gap = gap;
    }

    @Override
    public void layout() {

        if (axis == Axis.X) {
            int x = padding;
            for (Widget widget : container.getWidgets()) {
                widget.setX(x);
                x += widget.getWidth() + gap;
            }
        } else if (axis == Axis.Y) {
            int y = padding;
            for (Widget widget : container.getWidgets()) {
                widget.setY(y);
                y += widget.getHeight() + gap;
            }
        }
    }

}


package de.unratedfilms.guilib.extra;

import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;

/**
 * Positions all the widgets in a container either on top of each other (Y axis) or from left to right (X axis) with a specified gap between them.
 */
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

        int coord = padding;
        for (Widget widget : container.getWidgets()) {
            widget.setCoord(axis, coord);
            coord += widget.getExtent(axis) + gap;
        }
    }

}

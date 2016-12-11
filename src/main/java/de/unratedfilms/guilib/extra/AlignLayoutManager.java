
package de.unratedfilms.guilib.extra;

import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;

/**
 * Sets either the X or the Y coordinate of all widgets in a container to a specific value, thereby aligning the widgets.
 */
public class AlignLayoutManager implements LayoutManager {

    private final Container container;

    private final Axis      axis;
    private final int       coordinate;

    public AlignLayoutManager(Container container, Axis axis, int coordinate) {

        this.container = container;
        this.axis = axis;
        this.coordinate = coordinate;
    }

    @Override
    public void layout() {

        if (axis == Axis.X) {
            for (Widget widget : container.getWidgets()) {
                widget.setX(coordinate);
            }
        } else if (axis == Axis.Y) {
            for (Widget widget : container.getWidgets()) {
                widget.setY(coordinate);
            }
        }
    }

}

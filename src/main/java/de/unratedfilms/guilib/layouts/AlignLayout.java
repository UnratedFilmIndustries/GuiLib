
package de.unratedfilms.guilib.layouts;

import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;

/**
 * Sets either the X or the Y coordinate of all widgets in a container to a specific value, thereby aligning the widgets.
 */
public class AlignLayout implements LayoutManager {

    private final Axis axis;
    private final int  coordinate;

    public AlignLayout(Axis axis, int coordinate) {

        this.axis = axis;
        this.coordinate = coordinate;
    }

    @Override
    public void layout(Container container) {

        for (Widget widget : container.getWidgets()) {
            widget.setCoord(axis, coordinate);
        }
    }

}

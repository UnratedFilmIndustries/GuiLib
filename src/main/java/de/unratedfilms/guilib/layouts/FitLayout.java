
package de.unratedfilms.guilib.layouts;

import org.apache.commons.lang3.Validate;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;

/**
 * Sets either the width or the height of all widgets in a container to either a specific value or the width/height of the container itself,
 * thereby fitting all widgets into a specified space.
 */
public class FitLayout implements LayoutManager {

    private final Axis axis;
    private final int  extent;

    public FitLayout(Axis axis) {

        this.axis = axis;
        extent = -1;
    }

    public FitLayout(Axis axis, int extent) {

        Validate.isTrue(extent >= 0, "Width/height of widget must not be negative");

        this.axis = axis;
        this.extent = extent;
    }

    @Override
    public void layout(Container container) {

        if (extent == -1) {
            Validate.validState(container instanceof WidgetFlexible, "The fit layout manager without a fixed extent doesn't make any sense in a non-flexible container; did you create a container of the wrong type?");
        }

        container.getWidgets().stream().forEach(w -> Validate.validState(w instanceof WidgetFlexible, "The fit layout manager can only deal with flexible widgets, but '%s' isn't one", w));

        int effectiveExtent = extent == -1 ? container.getExtent(axis) : extent;
        for (Widget widget : container.getWidgets()) {
            ((WidgetFlexible) widget).setExtent(axis, effectiveExtent);
        }
    }

}

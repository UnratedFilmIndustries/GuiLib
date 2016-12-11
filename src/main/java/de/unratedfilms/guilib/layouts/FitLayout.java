
package de.unratedfilms.guilib.layouts;

import org.apache.commons.lang3.Validate;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;

public class FitLayout implements LayoutManager {

    private final Container container;

    private final Axis      axis;
    private final int       extent;

    public FitLayout(Container container, Axis axis, int extent) {

        this.container = container;
        this.axis = axis;
        this.extent = extent;
    }

    @Override
    public void layout() {

        container.getWidgets().stream().forEach(w -> Validate.validState(w instanceof WidgetFlexible, "The fit layout manager can only deal with flexible widgets, but '%s' isn't one", w));

        for (Widget widget : container.getWidgets()) {
            ((WidgetFlexible) widget).setExtent(axis, extent);
        }
    }

}

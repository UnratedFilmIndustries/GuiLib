
package de.unratedfilms.guilib.layouts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;

/**
 * Squeezes all of the widgets in a container either into its whole width (X axis) or height (Y axis), meaning that all the available space on that axis is used up.
 * Weights can be used to control how much of the space should be used by each widget. Widgets without a specific weight are weighted with the value 1.
 */
public class SqueezeLayout implements LayoutManager {

    private final Axis                axis;
    private final int                 padding, gap;

    private final Map<Widget, Double> weights = new HashMap<>();

    public SqueezeLayout(Axis axis) {

        this(axis, 10, 5);
    }

    public SqueezeLayout(Axis axis, int padding, int gap) {

        this.axis = axis;
        this.padding = padding;
        this.gap = gap;
    }

    private double getWeight(WidgetFlexible widget) {

        return weights.containsKey(widget) ? weights.get(widget) : 1;
    }

    public SqueezeLayout addWeight(WidgetFlexible widget, double weight) {

        Validate.notNull(widget, "Can't set the squeeze weight of a null widget");
        Validate.isTrue(weight > 0, "Can't use negative or zero values for squeeze layout weights");

        weights.put(widget, weight);

        return this;
    }

    @Override
    public void layout(Container container) {

        Validate.validState(container instanceof WidgetFlexible, "The squeeze layout manager doesn't make any sense in a non-flexible container; did you create a container of the wrong type?");

        container.getWidgets().stream().forEach(w -> Validate.validState(w instanceof WidgetFlexible, "The squeeze layout manager can only deal with flexible widgets, but '%s' isn't one", w));
        @SuppressWarnings ("unchecked")
        List<WidgetFlexible> widgets = (List<WidgetFlexible>) (List<?>) container.getWidgets();

        double weightSum = widgets.stream().mapToDouble(w -> getWeight(w)).sum();
        int availableSpace = container.getWidth() - 2 * padding;

        int coord = padding - gap / 2;
        for (WidgetFlexible widget : widgets) {
            widget.setCoord(axis, coord + gap / 2);

            double fraction = getWeight(widget) / weightSum;
            int ungappedExtent = (int) (fraction * availableSpace);
            widget.setExtent(axis, ungappedExtent - gap /* '2 * gap/2' */);

            coord += ungappedExtent;
        }
    }

}

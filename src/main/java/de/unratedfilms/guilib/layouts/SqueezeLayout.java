
package de.unratedfilms.guilib.layouts;

import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Squeezes all of the widgets in a container either into its whole width (X axis) or height (Y axis), meaning that all the available space on that axis is used up.
 * Weights can be used to control how much of the space should be used by each widget.
 * By instead adding a widget to the "keeps" category, you can ensure that it is not resized; instead, it will be directly integrated into the flow.
 * Any widgets without a specific weight are considered to be an error and will cause an exception.
 */
public class SqueezeLayout implements LayoutManager {

    private final Axis                        axis;
    private final int                         padding, gap;

    private final Set<Widget>                 keeps   = new HashSet<>();
    private final Map<WidgetFlexible, Double> weights = new HashMap<>();

    public SqueezeLayout(Axis axis) {

        this(axis, 10, 5);
    }

    public SqueezeLayout(Axis axis, int padding, int gap) {

        this.axis = axis;
        this.padding = padding;
        this.gap = gap;
    }

    public SqueezeLayout keep(Widget... widgets) {

        return keep(Arrays.asList(widgets));
    }

    public SqueezeLayout keep(Iterable<Widget> widgets) {

        Validate.noNullElements(widgets, "Can't mark null widgets as squeeze keeps");

        for (Widget widget : widgets) {
            Validate.isTrue(!weights.containsKey(widget), "Can't add a widget both as squeeze keep and sequeeze weight");
            keeps.add(widget);
        }

        return this;
    }

    public SqueezeLayout weight(double weight, WidgetFlexible... widgets) {

        return weight(weight, Arrays.asList(widgets));
    }

    public SqueezeLayout weight(double weight, Iterable<WidgetFlexible> widgets) {

        Validate.noNullElements(widgets, "Can't set the squeeze weight of a null widget");
        Validate.isTrue(weight > 0, "Can't use negative or zero values for squeeze layout weights");

        for (WidgetFlexible widget : widgets) {
            Validate.isTrue(!weights.containsKey(widget), "Can't add a widget both as squeeze keep and sequeeze weight");
            weights.put(widget, weight);
        }

        return this;
    }

    @Override
    public void layout(Container container) {

        Validate.validState(container instanceof WidgetFlexible, "The squeeze layout manager doesn't make any sense in a non-flexible container; did you create a container of the wrong type?");

        for (Widget widget : container.getWidgets()) {
            Validate.validState(keeps.contains(widget) || weights.containsKey(widget), "The widget '%s' neither is in the keeps nor the weights squeeze category; did you miss it?", widget);
        }

        double weightSum = weights.values().stream().mapToDouble(w -> w).sum();

        // The unavailable space is taken up by widgets that should be kept and not resized
        int unavailableSpace = container.getWidgets().stream().filter(w -> keeps.contains(w)).mapToInt(w -> w.getExtent(axis) + gap).sum();
        // This is all the space that can be used for the resized widgets; two half-gaps are added on each side for easier computations
        int availableSpace = container.getExtent(axis) + gap /* '2 * gap/2' */ - unavailableSpace - 2 * padding;

        int coord = padding - gap / 2;
        for (Widget widget : container.getWidgets()) {
            widget.setCoord(axis, coord + gap / 2);

            int extentWithGaps;
            if (weights.containsKey(widget)) {
                double fraction = weights.get(widget) / weightSum;
                extentWithGaps = (int) (fraction * availableSpace);
                ((WidgetFlexible) widget).setExtent(axis, extentWithGaps - gap /* '2 * gap/2' */);
            } else {
                extentWithGaps = widget.getExtent(axis) + gap; /* '2 * gap/2' */
            }

            coord += extentWithGaps;
        }
    }

}

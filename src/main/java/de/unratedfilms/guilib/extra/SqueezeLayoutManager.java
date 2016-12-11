
package de.unratedfilms.guilib.extra;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Container.LayoutManager;

public class SqueezeLayoutManager implements LayoutManager {

    private final Container        container;

    private final Axis             axis;
    private final int              padding, gap;

    private final List<Constraint> constraints = new ArrayList<>();

    public SqueezeLayoutManager(Container container, Axis axis) {

        this(container, axis, 10, 5);
    }

    public SqueezeLayoutManager(Container container, Axis axis, int padding, int gap) {

        this.container = container;
        this.axis = axis;
        this.padding = padding;
        this.gap = gap;
    }

    public SqueezeLayoutManager then(WidgetFlexible widget, double weight) {

        Validate.notNull(widget, "Can't add a null widget to the squeeze layout manager");
        Validate.isTrue(weight > 0, "Can't use negative or zero values for squeeze layout weights");

        constraints.add(new Constraint(widget, weight));

        return this;
    }

    @Override
    public void layout() {

        double weightSum = constraints.stream().mapToDouble(c -> c.weight).sum();

        if (axis == Axis.X) {
            int availableWidth = container.getWidth() - 2 * padding;

            int x = padding - gap / 2;
            for (Constraint c : constraints) {
                c.widget.setX(x + gap / 2);

                double fraction = c.weight / weightSum;
                int ungappedWidth = (int) (fraction * availableWidth);
                c.widget.setWidth(ungappedWidth - gap /* '2 * gap/2' */);

                x += ungappedWidth;
            }
        } else if (axis == Axis.Y) {
            // TODO: Y axis support
        }
    }

    private static class Constraint {

        private final WidgetFlexible widget;
        private final double         weight;

        private Constraint(WidgetFlexible widget, double weight) {

            this.widget = widget;
            this.weight = weight;
        }

    }

}

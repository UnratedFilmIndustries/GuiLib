
package de.unratedfilms.guilib.recursions;

import java.util.function.Consumer;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetParent;

public class WidgetTreeTraversalRecursion {

    public static enum TraversalOrder {

        PRE_ORDER, POST_ORDER;

    }

    public static void traverseWidgetTree(Widget root, TraversalOrder order, Consumer<Widget> consumer) {

        if (order == TraversalOrder.PRE_ORDER) {
            consumer.accept(root);
        }

        if (root instanceof WidgetParent) {
            for (Widget child : ((WidgetParent) root).getChildren()) {
                traverseWidgetTree(child, order, consumer);
            }
        }

        if (order == TraversalOrder.POST_ORDER) {
            consumer.accept(root);
        }
    }

    private WidgetTreeTraversalRecursion() {}

}

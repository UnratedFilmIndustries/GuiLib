
package de.unratedfilms.guilib.recursions;

import java.util.ArrayList;
import java.util.List;
import de.unratedfilms.guilib.core.WidgetFocusAware;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.core.WidgetParent;
import de.unratedfilms.guilib.recursions.WidgetTreeTraversalRecursion.TraversalOrder;

public class FocusShiftRecursion {

    public static void shiftFocus(WidgetParent widget, int shift) {

        // Find the currently focused widget, and abort if there isn't one
        WidgetFocusable oldFocusedWidget = findFocusCauser(widget);
        if (oldFocusedWidget == null) {
            return;
        }

        // Generate a list of all focusable widgets like text boxes, dropdowns etc.
        List<WidgetFocusable> list = new ArrayList<>();
        WidgetTreeTraversalRecursion.traverseWidgetTree(widget, TraversalOrder.PRE_ORDER,
                w -> {
                    if (w instanceof WidgetFocusable) {
                        list.add((WidgetFocusable) w);
                    }
                });

        // Get the index of the currently focused widget
        int oldFocusIdx = list.indexOf(oldFocusedWidget);

        // Get the widget which should gain focus as a result of the shift
        int newFocusIdx = (oldFocusIdx + shift + list.size()) % list.size();
        WidgetFocusable newFocusedWidget = list.get(newFocusIdx);

        // Execute the focus shift
        oldFocusedWidget.focusLost();
        newFocusedWidget.focusGained();
    }

    private static WidgetFocusable findFocusCauser(WidgetParent widget) {

        WidgetFocusAware focusedChild = widget.getFocusedChild();

        if (focusedChild instanceof WidgetFocusable) {
            return (WidgetFocusable) focusedChild;
        } else if (focusedChild instanceof WidgetParent) {
            return findFocusCauser((WidgetParent) focusedChild);
        } else {
            // Apparently, there's no focus causer
            return null;
        }
    }

    private FocusShiftRecursion() {}

}

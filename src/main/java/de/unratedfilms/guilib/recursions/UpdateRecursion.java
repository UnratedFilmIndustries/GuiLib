
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetParent;

public class UpdateRecursion {

    public static void update(Widget widget) {

        // Let the widget update
        widget.update();

        // If the widget is a parent, recursively update all its child widgets
        if (widget instanceof WidgetParent) {
            for (Widget child : ((WidgetParent) widget).getChildren()) {
                update(child);
            }
        }
    }

    private UpdateRecursion() {}

}

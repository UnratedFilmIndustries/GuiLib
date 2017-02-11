
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusAware;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.core.WidgetParent;

public class FocusLostRecursion {

    public static void focusLost(Widget widget) {

        if (widget instanceof WidgetFocusable) {
            WidgetFocusable focusable = (WidgetFocusable) widget;
            if (focusable.isFocused()) {
                focusable.focusLost();
            }
        } else if (widget instanceof WidgetParent) {
            WidgetFocusAware focusedChild = ((WidgetParent) widget).getFocusedChild();
            if (focusedChild != null) {
                focusLost(focusedChild);
            }
        }
    }

    private FocusLostRecursion() {}

}

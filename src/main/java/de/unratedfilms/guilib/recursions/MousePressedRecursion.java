
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusable;

public class MousePressedRecursion {

    public static boolean mousePressed(Widget widget, Viewport viewport, int mx, int my, MouseButton mouseButton) {

        return RecursionHelper.propagateEvent(widget, viewport,
                (w, vp) -> w.mousePressed(vp, mx, my, mouseButton),
                MousePressedRecursion::tryTransferFocus);
    }

    private static void tryTransferFocus(WidgetFocusable from, Widget to) {

        if (from != null) {
            from.focusLost();
        }

        if (to instanceof WidgetFocusable) {
            ((WidgetFocusable) to).focusGained();
        }
    }

    private MousePressedRecursion() {}

}

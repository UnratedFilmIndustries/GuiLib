
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusable;

public class MousePressedRecursion {

    /*
     * Returns the widget which has captured the event, or null if no widget has captured the event.
     */
    public static Widget mousePressed(Widget widget, Viewport viewport, int mx, int my, MouseButton mouseButton) {

        Widget catcher = RecursionHelper.propagateEvent(widget, viewport,
                (w, vp) -> w.mousePressed(vp, mx, my, mouseButton));

        // If either no widget caught the event (user clicked outside of all widgets), or if some focusable widget caught the event
        if (catcher == null || catcher instanceof WidgetFocusable) {
            // Defocus the previously focused widget
            FocusLostRecursion.focusLost(widget);

            // Focus the catcher, if there is one
            if (catcher != null) {
                ((WidgetFocusable) catcher).focusGained();
            }
        }

        return catcher;
    }

    private MousePressedRecursion() {}

}

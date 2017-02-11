
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;

public class MouseWheelRecursion {

    /*
     * Returns the widget which has captured the event, or null if no widget has captured the event.
     */
    public static Widget mouseWheel(Widget widget, Viewport viewport, int mx, int my, int delta) {

        return RecursionHelper.propagateEvent(widget, viewport,
                (w, vp) -> w.mouseWheel(vp, mx, my, delta));
    }

    private MouseWheelRecursion() {}

}

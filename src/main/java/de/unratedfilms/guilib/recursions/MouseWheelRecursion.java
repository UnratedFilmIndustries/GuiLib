
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;

public class MouseWheelRecursion {

    public static boolean mouseWheel(Widget widget, Viewport viewport, int mx, int my, int delta) {

        return RecursionHelper.propagateEvent(widget, viewport,
                (w, vp) -> w.mouseWheel(vp, mx, my, delta),
                null);
    }

    private MouseWheelRecursion() {}

}

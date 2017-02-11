
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;

public class MouseReleasedRecursion {

    public static void mouseReleased(Widget widget, Viewport viewport, int mx, int my, MouseButton mouseButton) {

        RecursionHelper.propagateEvent(widget, viewport,
                (w, vp) -> {
                    w.mouseReleased(vp, mx, my, mouseButton);
                    return false; // since no widget captures the event, it will be propagated to all widgets
                });
    }

    private MouseReleasedRecursion() {}

}

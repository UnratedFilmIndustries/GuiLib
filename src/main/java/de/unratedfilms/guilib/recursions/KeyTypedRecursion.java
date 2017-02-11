
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.Widget;

public class KeyTypedRecursion {

    /*
     * Returns the widget which has captured the event, or null if no widget has captured the event.
     */
    public static Widget keyTyped(Widget widget, char typedChar, int keyCode) {

        return RecursionHelper.propagateEvent(widget, null,
                (w, _vp) -> w.keyTyped(typedChar, keyCode));
    }

    private KeyTypedRecursion() {}

}

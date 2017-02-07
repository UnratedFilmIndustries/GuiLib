
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.Widget;

public class KeyTypedRecursion {

    public static boolean keyTyped(Widget widget, char typedChar, int keyCode) {

        return RecursionHelper.propagateEvent(widget, null,
                (w, _vp) -> w.keyTyped(typedChar, keyCode),
                null);
    }

    private KeyTypedRecursion() {}

}


package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.core.WidgetParent;

public class MousePressedRecursion {

    /*
     * Returns the widget which has captured the event, or null if no widget has captured the event.
     */
    public static Widget mousePressed(Widget widget, Viewport viewport, int mx, int my, MouseButton mouseButton) {

        Widget catcher = RecursionHelper.propagateEvent(widget, viewport,
                (w, vp) -> w.mousePressed(vp, mx, my, mouseButton));

        /*
         * Transfer the focus from the root widget to the catcher.
         * If some focusable widget caught the event, that widget will be focused.
         * If either no widget caught the event (user clicked outside of all widgets), or if a non-focusable widget caught the event, we might still need to defocus the previously focused widget.
         */
        transferFocus(widget, catcher);

        return catcher;
    }

    // Returns whether the given root widget has the clicked widget as child, grandchild, great-grandchild etc.
    private static boolean transferFocus(Widget root, Widget clicked) {

        // Recurse downwards along the widget tree
        if (root instanceof WidgetParent) {
            boolean clickedWidgetIsBelowMe = false;

            for (Widget child : ((WidgetParent) root).getChildren()) {
                // Just for clarification, of course only one of these recursive calls could possibly return true
                clickedWidgetIsBelowMe |= transferFocus(child, clicked);
            }

            if (clickedWidgetIsBelowMe) {
                return true; // (*)
            }
        }

        /*
         * If the widget is the clicked widget, focus it.
         * If the widget is not the clicked widget, defocus it, but only if it is not part of the path to the clicked widget (*).
         */
        if (root == clicked) {
            if (root instanceof WidgetFocusable && ! ((WidgetFocusable) root).isFocused()) {
                ((WidgetFocusable) root).focusGained();
            }
            return true;
        } else if (root instanceof WidgetFocusable && ((WidgetFocusable) root).isFocused()) {
            ((WidgetFocusable) root).focusLost();
        }

        return false;
    }

    private MousePressedRecursion() {}

}

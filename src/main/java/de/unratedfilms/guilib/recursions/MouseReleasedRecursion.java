
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.core.WidgetParent;

public class MouseReleasedRecursion {

    public static void mouseReleased(Widget widget, Viewport viewport, int mx, int my, MouseButton mouseButton) {

        // If this widget is a parent, recursively notify all its child widgets about the event, even before this widget is notified
        if (widget instanceof WidgetParent) {
            WidgetParent parent = (WidgetParent) widget;
            WidgetFocusable focusedChild = parent.getFocusedChild();

            // If we have a focused widget, it is allowed to handle the event first
            if (focusedChild != null) {
                mouseReleased(focusedChild, parent.getChildViewport(viewport, focusedChild), mx, my, mouseButton);
            }

            // If we don't have a focused widget or the focused widget is not interested in the event, allow the other widgets to handle it
            for (Widget child : parent.getChildren()) {
                if (child != focusedChild /* the focused widget already had its chance */ ) {
                    mouseReleased(child, parent.getChildViewport(viewport, child), mx, my, mouseButton);
                }
            }
        }

        widget.mousePressed(viewport, mx, my, mouseButton);
    }

    private MouseReleasedRecursion() {}

}

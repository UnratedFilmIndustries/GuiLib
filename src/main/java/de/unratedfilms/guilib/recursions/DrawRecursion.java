
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.core.WidgetParent;

public class DrawRecursion {

    public static void draw(Widget widget, Viewport viewport, int mx, int my) {

        // Let the widget render
        widget.draw(viewport, mx, my);

        // If the widget is a parent, recursively draw all its child widgets
        if (widget instanceof WidgetParent) {
            WidgetParent parent = (WidgetParent) widget;
            WidgetFocusable focusedChild = parent.getFocusedChild();

            // Recursively draw all child widgets apart from the focused one
            for (Widget child : parent.getChildren()) {
                if (child != focusedChild) {
                    draw(child, parent.getChildViewport(viewport, child), mx, my);
                }
            }

            // If we have a focused widget, it is drawn last so that it can "overdraw" all the other widgets
            if (focusedChild != null) {
                draw(focusedChild, parent.getChildViewport(viewport, focusedChild), mx, my);
            }
        }
    }

    private DrawRecursion() {}

}


package de.unratedfilms.guilib.recursions;

import java.util.function.BiFunction;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusAware;
import de.unratedfilms.guilib.core.WidgetParent;

class RecursionHelper {

    /*
     * Returns the widget which has captured the event, or null if no widget has captured the event.
     */
    public static Widget propagateEvent(Widget widget, Viewport viewport, BiFunction<Widget, Viewport, Boolean> eventHandler) {

        // If this widget is a parent, recursively notify all its child widgets about the event, even before this widget is notified
        if (widget instanceof WidgetParent) {
            WidgetParent parent = (WidgetParent) widget;
            WidgetFocusAware focusedChild = parent.getFocusedChild();

            // If we have a focused widget, it is allowed to handle the event first
            if (focusedChild != null) {
                Widget catcher = propagateEvent(focusedChild, subViewport(parent, viewport, focusedChild), eventHandler);
                if (catcher != null) {
                    return catcher; // event captured
                }
            }

            // If we don't have a focused widget or the focused widget is not interested in the event, allow the other widgets to handle it
            for (Widget child : parent.getChildren()) {
                if (child != focusedChild /* the focused widget already had its chance */) {
                    Widget catcher = propagateEvent(child, subViewport(parent, viewport, child), eventHandler);
                    if (catcher != null) {
                        return catcher; // event captured
                    }
                }
            }
        }

        if (eventHandler.apply(widget, viewport) /* event captured? */ ) {
            return widget;
        } else {
            return null;
        }
    }

    private static Viewport subViewport(WidgetParent parent, Viewport parentViewport, Widget child) {

        return parentViewport == null ? null : parent.getChildViewport(parentViewport, child);
    }

    private RecursionHelper() {}

}

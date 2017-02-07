
package de.unratedfilms.guilib.recursions;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.core.WidgetParent;

class RecursionHelper {

    public static boolean propagateEvent(Widget widget, Viewport viewport, BiFunction<Widget, Viewport, Boolean> eventHandler, BiConsumer<WidgetFocusable, Widget> focusTransferCallback) {

        // If this widget is a parent, recursively notify all its child widgets about the event, even before this widget is notified
        if (widget instanceof WidgetParent) {
            WidgetParent parent = (WidgetParent) widget;
            WidgetFocusable focusedChild = parent.getFocusedChild();

            // If we have a focused widget, it is allowed to handle the event first
            if (focusedChild != null && propagateEvent(focusedChild, subViewport(parent, viewport, focusedChild), eventHandler, focusTransferCallback)) {
                return true; // event captured
            }

            // If we don't have a focused widget or the focused widget is not interested in the event, allow the other widgets to handle it
            for (Widget child : parent.getChildren()) {
                if (child != focusedChild /* the focused widget already had its chance */ && propagateEvent(child, subViewport(parent, viewport, child), eventHandler, focusTransferCallback)) {
                    // If the player triggered the event on a focusable widget, inform the focus transfer callback about it
                    // It'll probably focus the new widget, and further makes sure that the previously focused widget isn't focused anymore
                    if (focusTransferCallback != null) {
                        focusTransferCallback.accept(focusedChild, child);
                    }

                    return true; // click captured
                }
            }

            // If the player didn't trigger an event on any focusable widget, inform the focus transfer callback about it
            if (focusTransferCallback != null) {
                focusTransferCallback.accept(focusedChild, null);
            }
        }

        return eventHandler.apply(widget, viewport);
    }

    private static Viewport subViewport(WidgetParent parent, Viewport parentViewport, Widget child) {

        return parentViewport == null ? null : parent.getChildViewport(parentViewport, child);
    }

    private RecursionHelper() {}

}

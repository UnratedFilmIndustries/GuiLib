
package de.unratedfilms.guilib.recursions;

import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.core.WidgetParent;
import de.unratedfilms.guilib.core.WidgetRigid;

public class RevalidationRecursion {

    public static void revalidate(Widget widget, Viewport viewport, boolean force) {

        // If this widget is a parent and houses rigid widgets, revalidate those rigid widgets and alle rigid widgets below them first
        boolean rigidChildRevalidated = false;
        if (widget instanceof WidgetParent) {
            for (Widget child : ((WidgetParent) widget).getChildren()) {
                if (child instanceof WidgetRigid) {
                    rigidChildRevalidated |= revalidateRigidWidgetTree((WidgetRigid) child, force);
                }
            }
        }

        // Then, revalidate this actual widget
        boolean thisRevalidated = false;
        if (widget instanceof WidgetRigid) {
            thisRevalidated = ((WidgetRigid) widget).revalidate(force || rigidChildRevalidated);
        } else if (widget instanceof WidgetFlexible) {
            thisRevalidated = ((WidgetFlexible) widget).revalidate(viewport, force || rigidChildRevalidated);
        }

        // Finally, if this widget is a parent, find any non-rigid widgets below it and start new revalidations on those
        if (widget instanceof WidgetParent) {
            descendToNonRigidWidgets((WidgetParent) widget, viewport, force || thisRevalidated);
        }
    }

    /*
     * In order to revalidate a rigid widget, we first have to revalidate all its rigid children (if it has any).
     * That means that this function recursively revalidates all rigid widgets in the subtree below the given rigid widget.
     */
    private static boolean revalidateRigidWidgetTree(WidgetRigid from, boolean force) {

        boolean childRevalidated = false;

        // In case the widget is a parent, revalidate all its rigid children which determine their sizes by themselves
        if (from instanceof WidgetParent) {
            for (Widget child : ((WidgetParent) from).getChildren()) {
                if (child instanceof WidgetRigid) {
                    childRevalidated |= revalidateRigidWidgetTree((WidgetRigid) child, force);
                }
            }
        }

        /*
         * Then, revalidate the widget itself.
         * In case it's a parent, it can now arrange the previously revalidated rigid children and even resize the flexible ones.
         */
        return from.revalidate(force || childRevalidated);
    }

    private static void descendToNonRigidWidgets(WidgetParent from, Viewport viewport, boolean force) {

        for (Widget child : from.getChildren()) {
            // When we come across a widget that's not rigid and therefore hasn't already been revalidated recursively, start a new revalidation on it
            if (! (child instanceof WidgetRigid)) {
                revalidate(child, from.getChildViewport(viewport, child), force);
            }
            // When we come across a rigid widget that's also a parent, descend further in order to potentially discover other flexible widgets
            else if (child instanceof WidgetParent /* && child is rigid */) {
                descendToNonRigidWidgets((WidgetParent) child, from.getChildViewport(viewport, child), force);
            }
        }
    }

    private RevalidationRecursion() {}

}

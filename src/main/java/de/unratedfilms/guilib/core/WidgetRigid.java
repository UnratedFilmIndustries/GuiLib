
package de.unratedfilms.guilib.core;

/**
 * A {@link Widget} that is rigid and therefore cannot be resized by outside callers.
 * Only the widget's own {@link #revalidate(boolean)} method can change the widget's size.<br>
 * <br>
 * For example, widgets like labels or checkboxes are rigid since their sizes are directly derived from the text they display, rendering any kind of external change absurd.
 */
public interface WidgetRigid extends Widget {

    /**
     * Revalidates this widget by layouting its contents again, <b>as well as changing its own size!</b><br>
     * <br>
     * This method should only perform a revalidation if either the widget has been invalidated beforehand (e.g. because the text of a label changed) or the force flag is set.
     * If the widget is invalid and has child widgets, those child widgets should also be given a chance to revalidate themselves.
     * If the widget is valid, but one of its child widgets has been revalidated, it should revalidate itself.
     *
     * @param force Whether the widget and all its child widget's must be revalidated, even if they weren't explicitly invalidated beforehand.
     * @return Whether a revalidation has been performed.
     */
    @Override
    public boolean revalidate(boolean force);

}

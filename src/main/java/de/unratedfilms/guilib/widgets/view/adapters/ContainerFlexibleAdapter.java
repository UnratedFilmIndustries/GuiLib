
package de.unratedfilms.guilib.widgets.view.adapters;

import java.util.ArrayList;
import java.util.List;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;

/**
 * A minimal implementation of {@link ContainerFlexible}, based on {@link ContainerAdapter}.
 */
public abstract class ContainerFlexibleAdapter extends ContainerAdapter implements ContainerFlexible {

    private final List<ViewportAwareLayoutManager> viewportAwareLayoutManagers = new ArrayList<>();

    public ContainerFlexibleAdapter(Widget... widgets) {

        super(widgets);
    }

    @Override
    public ContainerFlexibleAdapter appendLayoutManager(LayoutManager layoutManager) {

        super.appendLayoutManager(layoutManager);
        return this;
    }

    @Override
    public ContainerFlexible appendLayoutManager(ViewportAwareLayoutManager layoutManager) {

        viewportAwareLayoutManagers.add(layoutManager);
        return this;
    }

    /*
     * Event handlers
     */

    private boolean forceRevalidation;

    /*
     * This method actually doesn't do any revalidation yet.
     * The revalidation is saved for later,
     * when the postRevalidation() message arrives and informs us that all the widgets in the hierarchy above us have completed their revalidation.
     */
    @Override
    public boolean doRevalidation(boolean force) {

        forceRevalidation = force;
        return !valid || force;
    }

    // This is called after all the widgets in the hierarchy above us have completed their revalidation
    @Override
    public void postRevalidation(Viewport viewport) {

        performRevalidation(() -> revalidateThis(viewport), forceRevalidation);

        for (Widget widget : widgets) {
            widget.postRevalidation(viewport);
        }
    }

    protected void revalidateThis(Viewport viewport) {

        // Don't forget to call the "real" revalidateThis() since subclasses of this class might want to use it (for some reason)
        revalidateThis();

        for (LayoutManager layoutManager : layoutManagers) {
            layoutManager.layout(this);
        }

        for (ViewportAwareLayoutManager layoutManager : viewportAwareLayoutManagers) {
            layoutManager.layout(this, viewport);
        }
    }

}

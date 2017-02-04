
package de.unratedfilms.guilib.widgets.view.adapters;

import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.ContainerRigid;

/**
 * A minimal implementation of {@link ContainerRigid}, based on {@link ContainerAdapter}.
 */
public abstract class ContainerRigidAdapter extends ContainerAdapter implements ContainerRigid {

    public ContainerRigidAdapter(Widget... widgets) {

        super(widgets);
    }

    /*
     * Event handlers
     */

    @Override
    public boolean doRevalidation(boolean force) {

        return performRevalidation(this::revalidateThis, force);
    }

    @Override
    protected void revalidateThis() {

        for (LayoutManager layoutManager : layoutManagers) {
            layoutManager.layout(this);
        }
    }

    @Override
    public void postRevalidation(Viewport viewport) {

        for (Widget widget : widgets) {
            widget.postRevalidation(viewport);
        }
    }

}

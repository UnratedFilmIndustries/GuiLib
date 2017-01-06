
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetRigid;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.view.adapters.ContainerAdapter;

/**
 * A {@link Container} which adjusts its own {@link #getBounds() bounds} so that they always cover all the contained widgets.
 */
public class ContainerAdjustingImpl extends ContainerAdapter implements WidgetRigid {

    public ContainerAdjustingImpl(Widget... widgets) {

        super(widgets);
    }

    @Override
    protected void doRevalidate() {

        super.doRevalidate();

        int width = 0;
        int height = 0;

        for (Widget widget : getWidgets()) {
            width = Math.max(width, widget.getX() + widget.getWidth());
            height = Math.max(height, widget.getY() + widget.getHeight());
        }

        setSize(width, height);
    }

}


package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.view.adapters.ContainerFlexibleAdapter;

/**
 * Default implementation of {@link Container}, which clips all content that peeks out of the container's set bounds.
 */
public class ContainerClippingImpl extends ContainerFlexibleAdapter {

    public ContainerClippingImpl(Widget... widgets) {

        super(widgets);
    }

}

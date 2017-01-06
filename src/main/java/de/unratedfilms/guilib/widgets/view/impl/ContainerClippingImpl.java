
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.view.adapters.ContainerAdapter;

/**
 * Default implementation of {@link Container}, which clips all content that peeks out of the container's set bounds.
 */
public class ContainerClippingImpl extends ContainerAdapter implements ContainerFlexible {

    public ContainerClippingImpl(Widget... widgets) {

        super(widgets);
    }

}

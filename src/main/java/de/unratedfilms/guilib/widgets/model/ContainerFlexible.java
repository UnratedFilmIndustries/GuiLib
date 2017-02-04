
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.WidgetFlexible;

/**
 * Abstract representation of a {@link Container} that is {@link WidgetFlexible flexible} and therefore can be resized from outside.
 */
public interface ContainerFlexible extends Container, WidgetFlexible {

    @Override
    public ContainerFlexible appendLayoutManager(LayoutManager layoutManager);

    public ContainerFlexible appendLayoutManager(ViewportAwareLayoutManager layoutManager);

    public static interface ViewportAwareLayoutManager {

        public void layout(ContainerFlexible container, Viewport viewport);

    }

}

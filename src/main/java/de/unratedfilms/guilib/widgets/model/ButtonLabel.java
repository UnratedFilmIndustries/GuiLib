
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.WidgetFlexible;

/**
 * Abstract representation of a {@link Button} that displays a label text on its surface.
 */
public interface ButtonLabel extends Button, WidgetFlexible {

    public String getLabel();

    public void setLabel(String label);

}

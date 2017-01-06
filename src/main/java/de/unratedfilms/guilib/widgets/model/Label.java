
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.WidgetRigid;

/**
 * Abstract representation of a label that simply displays a line of text.
 */
public interface Label extends WidgetRigid {

    public String getText();

    public Label setText(String text);

}

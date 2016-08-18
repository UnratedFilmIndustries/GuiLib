
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.Widget;

/**
 * Abstract representation of a label that simply displays a line of text.
 */
public interface Label extends Widget {

    public String getText();

    public void setText(String text);

}

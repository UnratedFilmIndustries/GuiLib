
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.Widget;

/**
 * Abstract representation of a checkbox.
 */
public interface Checkbox extends Widget {

    public String getLabel();

    public void setLabel(String label);

    public boolean isChecked();

    public void setChecked(boolean checked);

}

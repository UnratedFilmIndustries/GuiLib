
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.WidgetRigid;

/**
 * Abstract representation of a checkbox.
 */
public interface Checkbox extends WidgetRigid {

    public CheckboxHandler getHandler();

    public Checkbox setHandler(CheckboxHandler handler);

    public String getLabel();

    public Checkbox setLabel(String label);

    public boolean isChecked();

    public Checkbox setChecked(boolean checked);

    public static interface CheckboxHandler {

        public void checkboxChanged(Checkbox checkbox, boolean checked);

    }

}

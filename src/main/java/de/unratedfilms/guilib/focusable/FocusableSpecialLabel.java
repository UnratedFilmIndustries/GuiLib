
package de.unratedfilms.guilib.focusable;

import de.unratedfilms.guilib.core.Widget;

/**
 * A FocusableLabel that is meant to display different text than what it really means in the background.
 */
public class FocusableSpecialLabel extends FocusableLabel {

    private String actualLabel;

    public FocusableSpecialLabel(String label, String actualLabel, Widget... tooltips) {

        super(label, tooltips);

        this.actualLabel = actualLabel;
    }

    public FocusableSpecialLabel(int x, int y, String label, String actualLabel, Widget... tooltips) {

        this(label, actualLabel, tooltips);

        setPosition(x, y);
    }

    public String getActualLabel() {

        return actualLabel;
    }

    public void setActualLabel(String actualLabel) {

        this.actualLabel = actualLabel;
    }

}


package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.WidgetRigid;

/**
 * Abstract representation of a scrollbar.
 */
public interface Scrollbar extends WidgetRigid {

    public Container getContainer();

    public void setContainer(Container container);

    /**
     * Returns the amount of pixels the scrollable widgets should be shifted upwards by.
     *
     * @return How many pixels to shift the scollable widgets.
     */
    public int getWidgetShift();

    /**
     * Sets the amount of pixels the scrollable widgets should be shifted upwards by.
     *
     * @param widgetShift How many pixels to shift the scollable widgets.
     */
    public void setWidgetShift(int widgetShift);

    /**
     * Shifts the scollable widgets upwards by {@code delta} pixels, taking into account the current {@link #getWidgetShift() widget shift}.
     *
     * @param delta How many pixels to "shift the shift" upwards by.
     */
    default public void addWidgetShift(int delta) {

        setWidgetShift(getWidgetShift() + delta);
    }

    /**
     * Shifts this scrollbar relative to its size + contentHeight.
     *
     * @param p Base pixels to shift.
     */
    public void addWidgetShiftRelative(int p);

}


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
     * Shifts the scollable widgets upwards by {@code p} pixels.
     *
     * @param p How many pixels to shift the scollable widgets.
     */
    public void addWidgetShift(int p);

    /**
     * Shifts this scrollbar relative to its size + contentHeight.
     *
     * @param p Base pixels to shift.
     */
    public void addWidgetShiftRelative(int p);

}

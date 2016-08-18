
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.integration.Container;

/**
 * Abstract representation of a scrollbar.
 */
public interface Scrollbar extends Widget {

    public Container getContainer();

    public void setContainer(Container container);

    /**
     * Shifts the scrollbar by {@code p} pixels.
     *
     * @param p How many pixels to shift the scrollbar.
     */
    public void shift(int p);

    /**
     * Shifts this scrollbar relative to its size + contentHeight.
     *
     * @param p Base pixels to shift.
     */
    public void shiftRelative(int p);

    public void revalidate(int topY, int bottomY);

    public void onChildRemoved();

}

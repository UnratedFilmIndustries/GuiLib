
package de.unratedfilms.guilib.core;

/**
 * A {@link Widget} that can gain and lose focus.
 */
public interface WidgetFocusable extends WidgetFocusAware {

    public void focusGained();

    public void focusLost();

}

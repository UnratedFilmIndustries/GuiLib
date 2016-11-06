
package de.unratedfilms.guilib.core;

/**
 * A {@link Widget} that can gain and lose focus.
 */
public interface WidgetFocusable extends Widget {

    public boolean isFocused();

    public void focusGained();

    public void focusLost();

}

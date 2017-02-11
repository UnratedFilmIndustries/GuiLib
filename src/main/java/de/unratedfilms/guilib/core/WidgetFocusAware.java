
package de.unratedfilms.guilib.core;

/**
 * A {@link Widget} that can be focused; it's focus state, however, is determined from somewhere else and not by a property inside the widget itself.
 * That's usually the behavior of {@link WidgetParent parent widgets} like containers.
 * If you want your widget to store its own focus property (i.e. because your programming a text field), take a look at {@link WidgetFocusable}.
 */
public interface WidgetFocusAware extends Widget {

    public boolean isFocused();

}

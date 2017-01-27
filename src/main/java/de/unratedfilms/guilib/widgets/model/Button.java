
package de.unratedfilms.guilib.widgets.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Widget;

/**
 * Abstract representation of a button.
 * The buttons calls {@link ButtonHandler#buttonClicked(Button, MouseButton)} when it is pressed.
 */
public interface Button extends Widget {

    public ButtonHandler getHandler();

    public Button setHandler(ButtonHandler handler);

    public boolean isEnabled();

    public Button setEnabled(boolean enabled);

    public static interface ButtonHandler {

        public void buttonClicked(Button button, MouseButton mouseButton);

    }

    public static class FilteredButtonHandler implements ButtonHandler {

        private final Collection<MouseButton> filter;
        private final ButtonHandler           wrapped;

        public FilteredButtonHandler(MouseButton filter, ButtonHandler wrapped) {

            this.filter = Collections.singleton(filter);
            this.wrapped = wrapped;
        }

        public FilteredButtonHandler(MouseButton[] filter, ButtonHandler wrapped) {

            this.filter = Arrays.asList(filter);
            this.wrapped = wrapped;
        }

        @Override
        public void buttonClicked(Button button, MouseButton mouseButton) {

            if (filter.contains(mouseButton)) {
                wrapped.buttonClicked(button, mouseButton);
            }
        }

    }

}

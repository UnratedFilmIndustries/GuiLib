
package de.unratedfilms.guilib.widgets.model;

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

    public static abstract class LeftButtonHandler implements ButtonHandler {

        @Override
        public void buttonClicked(Button button, MouseButton mouseButton) {

            if (mouseButton == MouseButton.LEFT) {
                leftButtonClicked(button);
            }
        }

        public abstract void leftButtonClicked(Button button);

    }

}

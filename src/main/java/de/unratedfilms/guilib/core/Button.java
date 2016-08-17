
package de.unratedfilms.guilib.core;

/**
 * Abstract representation of a button.
 * The buttons calls handler.buttonClicked(this) when it is pressed.
 */
public abstract class Button extends Widget {

    protected ButtonHandler handler;

    public Button(int width, int height, ButtonHandler handler) {

        super(width, height);

        this.handler = handler;
    }

    @Override
    public boolean click(int mx, int my, MouseButton mouseButton) {

        return mouseButton != MouseButton.UNKNOWN && enabled && inBounds(mx, my);
    }

    @Override
    public void handleClick(int mx, int my, MouseButton mouseButton) {

        if (handler != null) {
            handler.buttonClicked(this, mouseButton);
        }
    }

    public void setEnabled(boolean flag) {

        enabled = flag;
    }

    public String getLabel() {

        return "";
    }

    public void setLabel(String label) {

    }

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

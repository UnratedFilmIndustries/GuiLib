
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
    public boolean click(int mx, int my) {

        return enabled && inBounds(mx, my);
    }

    @Override
    public void handleClick(int mx, int my) {

        if (handler != null) {
            handler.buttonClicked(this);
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

        public void buttonClicked(Button button);

    }

}

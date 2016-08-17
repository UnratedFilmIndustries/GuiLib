
package de.unratedfilms.guilib.core;

/**
 * Abstract representation of a checkbox.
 */
public abstract class Checkbox extends Widget {

    private String  label;
    private boolean checked;

    public Checkbox(int width, int height, String label) {

        this(width, height, label, false);
    }

    public Checkbox(int width, int height, String label, boolean checked) {

        super(width, height);

        this.label = label;
        this.checked = checked;
    }

    @Override
    public boolean click(int mx, int my, MouseButton mouseButton) {

        return mouseButton == MouseButton.LEFT && inBounds(mx, my);
    }

    @Override
    public void handleClick(int mx, int my, MouseButton mouseButton) {

        checked = !checked;
    }

    public String getLabel() {

        return label;
    }

    public void setLabel(String label) {

        this.label = label;
    }

    public boolean isChecked() {

        return checked;
    }

    public void setChecked(boolean checked) {

        this.checked = checked;
    }

}

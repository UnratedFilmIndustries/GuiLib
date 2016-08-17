
package de.unratedfilms.guilib.core;

import net.minecraft.util.MathHelper;

/**
 * Abstract representation of a slider.
 */
public abstract class Slider extends Widget {

    private SliderFormat format;

    private float        value;

    protected boolean    dragging;

    public Slider(int width, int height, float value, SliderFormat format) {

        super(width, height);

        this.value = MathHelper.clamp_float(value, 0, 1);
        this.format = format;
    }

    public float getValue() {

        return value;
    }

    public void setValue(float value) {

        this.value = value;
    }

    public SliderFormat getFormat() {

        return format;
    }

    public void setFormat(SliderFormat format) {

        this.format = format;
    }

    @Override
    public boolean click(int mx, int my, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT && inBounds(mx, my)) {
            value = (float) (mx - (x + 4)) / (float) (width - 8);
            value = MathHelper.clamp_float(value, 0, 1);
            dragging = true;
            return true;
        }

        return false;
    }

    @Override
    public void handleClick(int mx, int my, MouseButton mouseButton) {

        value = (float) (mx - (x + 4)) / (float) (width - 8);
        value = MathHelper.clamp_float(value, 0, 1);
        dragging = true;
    }

    @Override
    public void mouseReleased(int mx, int my, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT) {
            dragging = false;
        }
    }

    public static interface SliderFormat {

        public String format(Slider slider);

    }

}

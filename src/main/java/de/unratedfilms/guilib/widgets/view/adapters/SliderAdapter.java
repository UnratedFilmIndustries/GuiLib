
package de.unratedfilms.guilib.widgets.view.adapters;

import org.apache.commons.lang3.Validate;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.Slider;

/**
 * A minimal implementation of {@link Slider} that doesn't contain any drawing code.
 *
 * @param <V> The type of number that can be chosen using the slider (e.g. int or float).
 */
public abstract class SliderAdapter<V> extends ContextHelperWidgetAdapter implements Slider<V> {

    private SliderHandler<V>        handler;

    private V                       minValue, maxValue;
    private Axis                    draggingAxis;
    private SliderLabelFormatter<V> labelFormatter;

    // A float from 0 to 1 which describes the current position of the slider from the most left position (0) to the most right one (1)
    private float                   degree;

    protected boolean               dragging;

    public SliderAdapter(V minValue, V maxValue, SliderLabelFormatter<V> labelFormatter, V value) {

        this.minValue = minValue;
        this.maxValue = maxValue;
        setLabelFormatter(labelFormatter);
        setValue(value);
    }

    @Override
    public SliderHandler<V> getHandler() {

        return handler;
    }

    @Override
    public SliderAdapter<V> setHandler(SliderHandler<V> handler) {

        this.handler = handler;
        return this;
    }

    @Override
    public V getMinValue() {

        return minValue;
    }

    @Override
    public SliderAdapter<V> setMinValue(V minValue) {

        this.minValue = minValue;
        return this;
    }

    @Override
    public V getMaxValue() {

        return maxValue;
    }

    @Override
    public SliderAdapter<V> setMaxValue(V maxValue) {

        this.maxValue = maxValue;
        return this;
    }

    @Override
    public Axis getDraggingAxis() {

        return draggingAxis;
    }

    @Override
    public SliderAdapter<V> setDraggingAxis(Axis draggingAxis) {

        Validate.notNull(draggingAxis, "Slider dragging axis cannot be null");
        this.draggingAxis = draggingAxis;
        return this;
    }

    @Override
    public SliderLabelFormatter<V> getLabelFormatter() {

        return labelFormatter;
    }

    @Override
    public SliderAdapter<V> setLabelFormatter(SliderLabelFormatter<V> textFormatter) {

        Validate.notNull(textFormatter, "Slider label formatter cannot be null");
        this.labelFormatter = textFormatter;

        return this;
    }

    @Override
    public V getValue() {

        return convertFromDegree(degree);
    }

    @Override
    public SliderAdapter<V> setValue(V value) {

        this.degree = MathHelper.clamp(convertToDegree(value), 0, 1);
        return this;
    }

    /**
     * Converts the given degree (range 0 - 1) to the corresponding value, which could be returned by {@link #getValue()}.
     * This method should also consider the {@link #getMinValue() min value} and {@link #getMaxValue() max value} bounds.
     * For example, if you implemented an integer slider, you would use this method to convert the degree to an int value inside the min/max range.<br>
     * <br>
     * Basically, this is the inverse operation of {@link #convertToDegree(Object)}.
     *
     * @param degree A float from 0 to 1 which describes the current position of the slider from the most left position (0) to the most right one (1).
     * @return The user converted value which will be returned by {@link #getValue()}.
     */
    protected abstract V convertFromDegree(float degree);

    /**
     * Converts the given value, which could be taken in by {@link #setValue(Object)}, to the corresponding degree (range 0 - 1).
     * This method should also consider the {@link #getMinValue() min value} and {@link #getMaxValue() max value} bounds.
     * For example, if you implemented an integer slider, you would use this method to convert the int value, which is inside the min/max range, to a degree.<br>
     * <br>
     * Basically, this is the inverse operation of {@link #convertFromDegree(float)}.
     *
     * @param value The user value which has been taken in by {@link #setValue(Object)}.
     * @return The converted float from 0 to 1 which describes the new current position of the slider from the most left position (0) to the most right one (1).
     *         If the returned degree is outside the range 0 - 1, it is {@link MathHelper#clamp(float, float, float) clamped} to those bounds.
     */
    protected abstract float convertToDegree(V value);

    public float getDegree() {

        return degree;
    }

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        if (dragging) {
            int mPos = draggingAxis == Axis.X ? lmx : lmy;
            float newDegree = (float) (mPos - (getCoord(draggingAxis) + 4)) / (getExtent(draggingAxis) - 8);
            newDegree = MathHelper.clamp(newDegree, 0, 1);

            if (newDegree != degree) {
                degree = newDegree;

                if (handler != null) {
                    handler.sliderMoved(this);
                }
            }
        }
    }

    @Override
    public boolean mousePressedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT && inLocalBounds(viewport, lmx, lmy)) {
            dragging = true;
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            return true;
        }

        return false;
    }

    @Override
    public void mouseReleasedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT) {
            dragging = false;
        }
    }

}

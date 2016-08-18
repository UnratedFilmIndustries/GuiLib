
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.widgets.model.Slider;

/**
 * A vanilla-style {@link Slider} that allows the input of arbitrary decimal numbers.
 */
public class SliderDecimalImpl extends SliderImpl<Float> {

    public SliderDecimalImpl(SliderLabelFormatter<Float> textFormatter, float value) {

        this(150, 20, textFormatter, value);
    }

    public SliderDecimalImpl(int width, int height, SliderLabelFormatter<Float> textFormatter, float value) {

        this(width, height, 0, 1, textFormatter, value);
    }

    public SliderDecimalImpl(float minValue, float maxValue, SliderLabelFormatter<Float> textFormatter, float value) {

        this(150, 20, minValue, maxValue, textFormatter, value);
    }

    public SliderDecimalImpl(int width, int height, float minValue, float maxValue, SliderLabelFormatter<Float> textFormatter, float value) {

        super(width, height, minValue, maxValue, textFormatter, value);
    }

    @Override
    protected Float convertFromDegree(float degree) {

        return degree * (getMaxValue() - getMinValue()) + getMinValue();
    }

    @Override
    protected float convertToDegree(Float value) {

        return (value - getMinValue()) / (getMaxValue() - getMinValue());
    }

}

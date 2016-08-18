
package de.unratedfilms.guilib.widgets.view.impl;

import de.unratedfilms.guilib.widgets.model.Slider;

/**
 * A vanilla-style {@link Slider} that allows the input of arbitrary integer numbers.
 */
public class SliderIntegerImpl extends SliderImpl<Integer> {

    public SliderIntegerImpl(int minValue, int maxValue, SliderLabelFormatter<Integer> textFormatter, int value) {

        this(150, 20, minValue, maxValue, textFormatter, value);
    }

    public SliderIntegerImpl(int width, int height, int minValue, int maxValue, SliderLabelFormatter<Integer> textFormatter, int value) {

        super(width, height, minValue, maxValue, textFormatter, value);
    }

    @Override
    protected Integer convertFromDegree(float degree) {

        return Math.round(degree * (getMaxValue() - getMinValue()) + getMinValue());
    }

    @Override
    protected float convertToDegree(Integer value) {

        return (float) (value - getMinValue()) / (getMaxValue() - getMinValue());
    }

}

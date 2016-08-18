
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.Widget;

/**
 * Abstract representation of a slider.
 *
 * @param <V> The type of number that can be chosen using the slider (e.g. int or float).
 */
public interface Slider<V> extends Widget {

    public V getMinValue();

    public void setMinValue(V minValue);

    public V getMaxValue();

    public void setMaxValue(V maxValue);

    public SliderLabelFormatter<V> getLabelFormatter();

    public void setLabelFormatter(SliderLabelFormatter<V> labelFormatter);

    public V getValue();

    public void setValue(V value);

    public static interface SliderLabelFormatter<V> {

        public String formatLabel(Slider<V> slider);

    }

}

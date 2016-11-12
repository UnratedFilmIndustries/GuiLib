
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.WidgetFlexible;

/**
 * Abstract representation of a slider.
 *
 * @param <V> The type of number that can be chosen using the slider (e.g. int or float).
 */
public interface Slider<V> extends WidgetFlexible {

    public V getMinValue();

    public Slider<V> setMinValue(V minValue);

    public V getMaxValue();

    public Slider<V> setMaxValue(V maxValue);

    public SliderLabelFormatter<V> getLabelFormatter();

    public Slider<V> setLabelFormatter(SliderLabelFormatter<V> labelFormatter);

    public V getValue();

    public Slider<V> setValue(V value);

    public static interface SliderLabelFormatter<V> {

        public String formatLabel(Slider<V> slider);

    }

}

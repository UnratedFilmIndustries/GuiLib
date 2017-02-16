
package de.unratedfilms.guilib.widgets.model;

import java.awt.Color;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.core.WidgetFocusable;

/**
 * Abstract representation of a color picker that allows you to choose a traditional RGB color value.
 * Note that this widget should act like a button which you can click in order to open up the actual color picker overlay, just like dropdown does it.
 */
public interface ColorPicker extends WidgetFlexible, WidgetFocusable {

    public ColorPickerHandler getHandler();

    public ColorPicker setHandler(ColorPickerHandler handler);

    /**
     * Returns the currently picked {@link Color}.
     * Note that you can easily convert this into a minecraft-compatible color integer by calling {@link Color#getRGB()} on the returned color object.
     *
     * @return The current color.
     */
    public Color getColor();

    /**
     * Returns the currently picked {@link #getColor() color} as a minecraft-compatible color integer.
     * If you really wanted to, you could also call {@link #getColor()} and then {@link Color#getRGB()} to achieve the same result.
     *
     * @return The current color as a single integer.
     */
    public int getColorBits();

    public ColorPicker setColor(Color color);

    public ColorPicker setColorBits(int colorBits);

    public static interface ColorPickerHandler {

        public void colorPicked(ColorPicker colorPicker);

    }

}

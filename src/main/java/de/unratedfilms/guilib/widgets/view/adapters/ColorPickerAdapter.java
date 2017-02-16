
package de.unratedfilms.guilib.widgets.view.adapters;

import java.awt.Color;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.ColorPicker;

/**
 * A minimal implementation of {@link ColorPicker} that doesn't contain any drawing code.
 */
public abstract class ColorPickerAdapter extends ContextHelperWidgetAdapter implements ColorPicker {

    private ColorPickerHandler handler;

    private int                colorBits = 0x00_00_00_00;
    private Color              colorObjCache;

    private boolean            focused;

    @Override
    public ColorPickerHandler getHandler() {

        return handler;
    }

    @Override
    public ColorPickerAdapter setHandler(ColorPickerHandler handler) {

        this.handler = handler;
        return this;
    }

    @Override
    public Color getColor() {

        if (colorObjCache == null) {
            colorObjCache = new Color(colorBits, true);
        }
        return colorObjCache;
    }

    @Override
    public int getColorBits() {

        return colorBits;
    }

    @Override
    public ColorPickerAdapter setColor(Color color) {

        colorBits = color.getRGB();
        colorObjCache = color;
        return this;
    }

    @Override
    public ColorPickerAdapter setColorBits(int colorBits) {

        this.colorBits = colorBits;
        colorObjCache = null;
        return this;
    }

    @Override
    public boolean isFocused() {

        return focused;
    }

    @Override
    public void focusGained() {

        focused = true;
    }

    @Override
    public void focusLost() {

        focused = false;
    }

    @Override
    public boolean mousePressedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT && inLocalBounds(viewport, lmx, lmy)) {
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }

        return false;
    }

}

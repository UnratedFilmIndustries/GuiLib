
package de.unratedfilms.guilib.widgets.view.adapters;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.WidgetAdapter;
import de.unratedfilms.guilib.widgets.model.Checkbox;

/**
 * A minimal implementation of {@link Checkbox} that doesn't contain any drawing code.
 */
public abstract class CheckboxAdapter extends WidgetAdapter implements Checkbox {

    private String  label;
    private boolean checked;

    public CheckboxAdapter(int width, int height, String label) {

        this(width, height, label, false);
    }

    public CheckboxAdapter(int width, int height, String label, boolean checked) {

        super(width, height);

        setLabel(label);
        this.checked = checked;
    }

    @Override
    public String getLabel() {

        return label;
    }

    @Override
    public void setLabel(String label) {

        Validate.notNull(label, "Checkbox label cannot be null");
        this.label = label;
    }

    @Override
    public boolean isChecked() {

        return checked;
    }

    @Override
    public void setChecked(boolean checked) {

        this.checked = checked;
    }

    @Override
    public boolean mousePressed(int mx, int my, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT && inBounds(mx, my)) {
            MC.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));

            checked = !checked;
            return true;
        }

        return false;
    }

}

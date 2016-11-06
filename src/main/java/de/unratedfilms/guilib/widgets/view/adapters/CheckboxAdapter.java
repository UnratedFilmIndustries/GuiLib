
package de.unratedfilms.guilib.widgets.view.adapters;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.Checkbox;

/**
 * A minimal implementation of {@link Checkbox} that doesn't contain any drawing code.
 */
public abstract class CheckboxAdapter extends ContextHelperWidgetAdapter implements Checkbox {

    private String  label;
    private boolean checked;

    public CheckboxAdapter(String label) {

        this(label, false);
    }

    public CheckboxAdapter(String label, boolean checked) {

        setLabel(label);
        this.checked = checked;
    }

    @Override
    public String getLabel() {

        return label;
    }

    @Override
    public void setLabel(String label) {

        this.label = label;
        invalidate();
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
    public boolean mousePressedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

        if (mouseButton == MouseButton.LEFT && inLocalBounds(viewport, lmx, lmy)) {
            MC.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));

            checked = !checked;
            return true;
        }

        return false;
    }

}

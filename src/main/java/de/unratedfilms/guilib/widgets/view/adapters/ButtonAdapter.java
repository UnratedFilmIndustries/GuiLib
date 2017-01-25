
package de.unratedfilms.guilib.widgets.view.adapters;

import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.Button;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

/**
 * A minimal implementation of {@link Button} that doesn't contain any drawing code.
 */
public abstract class ButtonAdapter extends ContextHelperWidgetAdapter implements Button {

    private ButtonHandler handler;
    private boolean       enabled = true;

    public ButtonAdapter(ButtonHandler handler) {

        this.handler = handler;
    }

    @Override
    public ButtonHandler getHandler() {

        return handler;
    }

    @Override
    public ButtonAdapter setHandler(ButtonHandler handler) {

        this.handler = handler;
        return this;
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }

    @Override
    public ButtonAdapter setEnabled(boolean enabled) {

        this.enabled = enabled;
        return this;
    }

    @Override
    public boolean mousePressedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

        if (enabled && mouseButton != MouseButton.UNKNOWN && inLocalBounds(viewport, lmx, lmy)) {
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            if (handler != null) {
                handler.buttonClicked(this, mouseButton);
            }

            return true;
        }

        return false;
    }

}

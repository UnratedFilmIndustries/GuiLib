
package de.unratedfilms.guilib.widgets.view.adapters;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.WidgetAdapter;
import de.unratedfilms.guilib.widgets.model.Button;

/**
 * A minimal implementation of {@link Button} that doesn't contain any drawing code.
 */
public abstract class ButtonAdapter extends WidgetAdapter implements Button {

    private ButtonHandler handler;
    private boolean       enabled = true;

    public ButtonAdapter(int width, int height) {

        super(width, height);
    }

    public ButtonAdapter(int width, int height, ButtonHandler handler) {

        super(width, height);

        this.handler = handler;
    }

    @Override
    public ButtonHandler getHandler() {

        return handler;
    }

    @Override
    public void setHandler(ButtonHandler handler) {

        this.handler = handler;
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }

    @Override
    public boolean mousePressed(int mx, int my, MouseButton mouseButton) {

        if (enabled && mouseButton != MouseButton.UNKNOWN && inBounds(mx, my)) {
            MC.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));

            if (handler != null) {
                handler.buttonClicked(this, mouseButton);
            }

            return true;
        }

        return false;
    }

}

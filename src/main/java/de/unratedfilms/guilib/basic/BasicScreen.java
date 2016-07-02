
package de.unratedfilms.guilib.basic;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import de.unratedfilms.guilib.core.Button;
import de.unratedfilms.guilib.core.Button.ButtonHandler;
import de.unratedfilms.guilib.core.Container;
import de.unratedfilms.guilib.core.Widget;

/**
 * The core GuiScreen. Use this class for your GUIs.
 */
public abstract class BasicScreen extends GuiScreen {

    private final GuiScreen   parent;
    private boolean           hasInit, closed;
    protected List<Container> containers;
    protected Container       selectedContainer;

    public BasicScreen(GuiScreen parent) {

        this.parent = parent;
        containers = new ArrayList<>();
    }

    public GuiScreen getParent() {

        return parent;
    }

    public List<Container> getContainers() {

        return containers;
    }

    /**
     * Called ONCE to create this GUI.
     * Create your containers and widgets here.
     */
    protected abstract void createGui();

    /**
     * Revalidate this GUI.
     * Reset your widget locations/dimensions here.
     */
    protected abstract void revalidateGui();

    /**
     * Called when this GUI is reopened after being closed.
     */
    protected abstract void reopenedGui();

    public void close() {

        mc.displayGuiScreen(parent);
    }

    /**
     * Called when the selectedContainer did not capture this keyboard event.
     *
     * @param typedChar Character typed (if any)
     * @param keyCode Keyboard.KEY_ code for this key
     */
    protected void unhandledKeyTyped(char typedChar, int keyCode) {

    }

    /**
     * Called to draw this screen's background
     */
    protected void drawBackground() {

        drawDefaultBackground();
    }

    public void drawCenteredStringNoShadow(FontRenderer ft, String str, int cx, int y, int color) {

        ft.drawString(str, cx - ft.getStringWidth(str) / 2, y, color);
    }

    /*
     * Listeners
     */
    @Override
    public void initGui() {

        Keyboard.enableRepeatEvents(true);

        if (!hasInit) {
            createGui();
            hasInit = true;
        }

        revalidateGui();

        if (closed) {
            reopenedGui();
            closed = false;
        }
    }

    @Override
    public void onGuiClosed() {

        closed = true;
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {

        for (Container c : containers) {
            c.update();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawBackground();
        List<Widget> overlays = new ArrayList<>();
        int scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
        for (Container c : containers) {
            overlays.addAll(c.draw(mouseX, mouseY, scale));
        }
        for (Widget w : overlays) {
            w.draw(mouseX, mouseY);
        }
    }

    /**
     * See {@link GuiScreen#handleMouseInput} for more information about mouseX and mouseY.
     */
    @Override
    public void handleMouseInput() {

        super.handleMouseInput();

        int delta = Mouse.getEventDWheel();
        if (delta != 0) {
            int mouseX = Mouse.getEventX() * width / mc.displayWidth;
            int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
            boolean handled = false;
            delta = MathHelper.clamp_int(delta, -5, 5);

            for (Container c : containers) {
                if (c.inBounds(mouseX, mouseY)) {
                    c.mouseWheel(delta);
                    handled = true;
                    break;
                }
            }
            if (!handled && selectedContainer != null) {
                selectedContainer.mouseWheel(delta);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int code) {

        if (code == 0) {
            for (Container c : containers) {
                if (c.mouseClicked(mouseX, mouseY)) {
                    selectedContainer = c;
                    break;
                }
            }

            for (Container c : containers) {
                if (c != selectedContainer) {
                    c.setFocused(null);
                }
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int code) {

        if (code == 0) {
            for (Container c : containers) {
                c.mouseReleased(mouseX, mouseY);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {

        boolean handled = selectedContainer != null ? selectedContainer.keyTyped(typedChar, keyCode) : false;
        if (!handled) {
            unhandledKeyTyped(typedChar, keyCode);
        }
    }

    public class CloseButtonHandler implements ButtonHandler {

        @Override
        public void buttonClicked(Button button) {

            close();
        }

    }

}

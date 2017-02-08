
package de.unratedfilms.guilib.integration;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.MathHelper;
import de.unratedfilms.guilib.core.Dimension;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.recursions.DrawRecursion;
import de.unratedfilms.guilib.recursions.KeyTypedRecursion;
import de.unratedfilms.guilib.recursions.MousePressedRecursion;
import de.unratedfilms.guilib.recursions.MouseReleasedRecursion;
import de.unratedfilms.guilib.recursions.MouseWheelRecursion;
import de.unratedfilms.guilib.recursions.RevalidationRecursion;
import de.unratedfilms.guilib.recursions.UpdateRecursion;

/**
 * The core GuiScreen. Use this class for your GUIs.
 */
public abstract class BasicScreen extends GuiScreen {

    private final GuiScreen parent;
    private boolean         hasInit, closed;
    private WidgetFlexible  rootWidget;

    /*
     * The size of this screen.
     * Basically, the biggest available viewport that drawing is allowed on.
     */
    private Viewport        rootViewport;

    public BasicScreen(GuiScreen parent) {

        this.parent = parent;
    }

    public GuiScreen getParent() {

        return parent;
    }

    public WidgetFlexible getRootWidget() {

        return rootWidget;
    }

    public void setRootWidget(WidgetFlexible rootWidget) {

        this.rootWidget = rootWidget;
    }

    protected Viewport getRootViewport() {

        return rootViewport;
    }

    public void close() {

        mc.displayGuiScreen(parent);
    }

    /**
     * Called to draw this screen's background
     */
    protected void drawBackground() {

        drawDefaultBackground();
    }

    /*
     * Listeners for the events send by minecraft
     */

    @Override
    public void initGui() {

        Keyboard.enableRepeatEvents(true);

        if (!hasInit) {
            createGui();
            hasInit = true;
        }

        // Window resize root viewport adjustment
        rootViewport = new Viewport(new Dimension(width, height));

        // Window resize revalidation
        rootWidget.setBounds(0, 0, width, height);
        RevalidationRecursion.revalidate(rootWidget, rootViewport, true);

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

        UpdateRecursion.update(rootWidget);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        // Potential revalidation
        RevalidationRecursion.revalidate(rootWidget, rootViewport, false);

        drawBackground();
        DrawRecursion.draw(rootWidget, rootViewport, mouseX, mouseY);
    }

    /**
     * See {@link GuiScreen#handleMouseInput} for more information about mouseX and mouseY.
     */
    @Override
    public void handleMouseInput() {

        try {
            super.handleMouseInput();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int delta = Mouse.getEventDWheel();
        if (delta != 0) {
            int mouseX = Mouse.getEventX() * width / mc.displayWidth;
            int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
            delta = MathHelper.clamp(delta, -5, 5);

            MouseWheelRecursion.mouseWheel(rootWidget, rootViewport, mouseX, mouseY, delta);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int code) {

        MousePressedRecursion.mousePressed(rootWidget, rootViewport, mouseX, mouseY, MouseButton.fromCode(code));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int code) {

        MouseReleasedRecursion.mouseReleased(rootWidget, rootViewport, mouseX, mouseY, MouseButton.fromCode(code));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {

        boolean handled = KeyTypedRecursion.keyTyped(rootWidget, typedChar, keyCode);

        if (!handled) {
            unhandledKeyTyped(typedChar, keyCode);
        }
    }

    /*
     * Event handler stubs for the events sent by this class
     */

    /**
     * Called ONCE to create this GUI.
     * Create your containers and widgets here.
     */
    protected abstract void createGui();

    /**
     * Called when this GUI is reopened after being closed.
     */
    protected void reopenedGui() {

    }

    /**
     * Called when the selectedContainer did not capture this keyboard event.
     *
     * @param typedChar Character typed (if any)
     * @param keyCode Keyboard.KEY_ code for this key
     */
    protected void unhandledKeyTyped(char typedChar, int keyCode) {

        // Custom screens can override this behavior if they want to
        if (keyCode == Keyboard.KEY_ESCAPE) {
            close();
        }
    }

}

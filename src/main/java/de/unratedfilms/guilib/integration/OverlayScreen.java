
package de.unratedfilms.guilib.integration;

import net.minecraft.client.Minecraft;

public abstract class OverlayScreen extends BasicScreen {

    // Note that this is a BasicScreen instead of a generic GuiScreen
    private final BasicScreen parent;

    /**
     * Creates a new overlay screen using the {@link Minecraft#currentScreen currently shown GUI screen}.
     * Note that this method assumes that the currently open GUI screen is an instance of {@link BasicScreen}.
     * If that's not the case, an exception will be thrown.
     */
    public OverlayScreen() {

        this((BasicScreen) Minecraft.getMinecraft().currentScreen);
    }

    public OverlayScreen(BasicScreen parent) {

        super(parent);

        this.parent = parent;
    }

    @Override
    public BasicScreen getParent() {

        return parent;
    }

    @Override
    public boolean doesGuiPauseGame() {

        return getParent().doesGuiPauseGame();
    }

    @Override
    protected void revalidateGui() {

        parent.width = width;
        parent.height = height;
        parent.revalidateGui();
    }

    @Override
    public void drawBackground() {

        parent.drawScreen(-1, -1, 0);
    }

}

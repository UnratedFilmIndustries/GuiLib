
package de.unratedfilms.guilib.basic;

public abstract class OverlayScreen extends BasicScreen {

    protected BasicScreen background;

    public OverlayScreen(BasicScreen background) {

        super(background);

        this.background = background;
    }

    @Override
    public void drawBackground() {

        background.drawScreen(-1, -1, 0);
    }

    @Override
    protected void revalidateGui() {

        background.width = width;
        background.height = height;
        background.revalidateGui();
    }

    @Override
    protected void reopenedGui() {

    }

}

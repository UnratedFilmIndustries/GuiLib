
package de.unratedfilms.guilib.extra;

import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Viewport.Drawer;
import de.unratedfilms.guilib.core.WidgetAdapter;

public abstract class ContextHelperWidgetAdapter extends WidgetAdapter {

    /*
     * Event handler helpers
     */

    @Override
    public void draw(final Viewport viewport, int mx, int my) {

        // First local context, ...
        viewport.drawInLocalContext(mx, my, new Drawer() {

            @Override
            public void draw(int lmx, int lmy) {

                drawInLocalContext(viewport, lmx, lmy);
            }

        });

        // ... then global context
        drawInGlobalContext(viewport, mx, my);
    }

    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

    }

    public void drawInGlobalContext(Viewport viewport, int mx, int my) {

    }

    @Override
    public boolean mousePressed(Viewport viewport, int mx, int my, MouseButton mouseButton) {

        // First global context, then local context
        return mousePressedInGlobalContext(viewport, mx, my, mouseButton) || mousePressedInLocalContext(viewport, viewport.localX(mx), viewport.localY(my), mouseButton);
    }

    public boolean mousePressedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

        return false;
    }

    public boolean mousePressedInGlobalContext(Viewport viewport, int mx, int my, MouseButton mouseButton) {

        return false;
    }

    @Override
    public void mouseReleased(Viewport viewport, int mx, int my, MouseButton mouseButton) {

        // First global context, then local context
        mouseReleasedInGlobalContext(viewport, mx, my, mouseButton);
        mouseReleasedInLocalContext(viewport, viewport.localX(mx), viewport.localY(my), mouseButton);

    }

    public void mouseReleasedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

    }

    public void mouseReleasedInGlobalContext(Viewport viewport, int mx, int my, MouseButton mouseButton) {

    }

    @Override
    public boolean mouseWheel(Viewport viewport, int mx, int my, int delta) {

        // First global context, then local context
        return mouseWheelInGlobalContext(viewport, mx, my, delta) || mouseWheelInLocalContext(viewport, viewport.localX(mx), viewport.localY(my), delta);
    }

    public boolean mouseWheelInLocalContext(Viewport viewport, int lmx, int lmy, int delta) {

        return false;
    }

    public boolean mouseWheelInGlobalContext(Viewport viewport, int mx, int my, int delta) {

        return false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {

        return false;
    }

}


package de.unratedfilms.guilib.extra;

import de.unratedfilms.guilib.integration.BasicScreen;
import de.unratedfilms.guilib.widgets.model.Button;
import de.unratedfilms.guilib.widgets.model.Button.LeftButtonHandler;

public class CloseScreenButtonHandler extends LeftButtonHandler {

    private final BasicScreen screen;

    public CloseScreenButtonHandler(BasicScreen screen) {

        this.screen = screen;
    }

    @Override
    public void leftButtonClicked(Button button) {

        screen.close();
    }

}

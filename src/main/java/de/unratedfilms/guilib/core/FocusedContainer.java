
package de.unratedfilms.guilib.core;

import de.unratedfilms.guilib.focusable.FocusableWidget;

/**
 * A "focused" version of a Container.
 * This container will always have a focused widget as long as there is a focusable widget contained.
 */
public class FocusedContainer extends Container {

    public FocusedContainer() {

        super();
    }

    public FocusedContainer(Scrollbar scrollbar, int shiftAmount, int extraScrollHeight) {

        super(scrollbar, shiftAmount, extraScrollHeight);
    }

    @Override
    public void setFocused(FocusableWidget f) {

        if (f != null) {
            super.setFocused(f);
        }
    }

    @Override
    public void addWidgets(Widget... widgets) {

        super.addWidgets(widgets);

        if (!hasFocusedWidget() && getFocusableWidgets().size() > 0) {
            setFocused(getFocusableWidgets().get(0));
        }
    }

}

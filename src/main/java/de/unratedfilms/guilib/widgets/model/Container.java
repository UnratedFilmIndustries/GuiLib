
package de.unratedfilms.guilib.widgets.model;

import java.util.List;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFocusable;

/**
 * Abstract representation of a container that holds a list of other {@link Widget}s and renders them whenever it is rendered.
 * The catch is that the {@link Viewport} of the child widgets is limited by the bounds of the container.
 * Thereby, the coordinates of the widgets are shifted by the coordinates of the container.
 * That means that you can group multiple widgets together and describe their relative coordinates to each other instead of absolute coordinates, because the container takes care of that for you.
 */
public interface Container extends Widget, WidgetFocusable {

    public Container appendLayoutManager(LayoutManager layoutManager);

    public List<Widget> getWidgets();

    public List<WidgetFocusable> getFocusableWidgets();

    public void addWidget(Widget widget);

    public void addWidgets(Widget... widgets);

    public WidgetFocusable getFocusedWidget();

    public static interface LayoutManager {

        public void layout();

    }

}

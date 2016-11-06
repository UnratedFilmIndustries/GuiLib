
package de.unratedfilms.guilib.widgets.view.adapters;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.TextTooltip;

/**
 * A minimal implementation of {@link TextTooltip} that doesn't contain any drawing code.
 */
public abstract class TextTooltipAdapter extends ContextHelperWidgetAdapter implements TextTooltip {

    protected static int getMaxStringWidth(List<String> strings) {

        int max = 0;
        for (String s : strings) {
            int width = MC.fontRenderer.getStringWidth(s);
            if (width > max) {
                max = width;
            }
        }
        return max;
    }

    private final List<String> lines = new ArrayList<>();

    public TextTooltipAdapter(List<String> lines) {

        setLines(lines);
    }

    @Override
    public List<String> getLines() {

        return lines;
    }

    @Override
    public void setLines(List<String> lines) {

        Validate.noNullElements(lines, "No tooltip line is allowed to be null");

        this.lines.clear();
        this.lines.addAll(lines);
        invalidate();
    }

    @Override
    public void addLines(List<String> lines) {

        Validate.noNullElements(lines, "No tooltip line is allowed to be null");

        this.lines.addAll(lines);
        invalidate();
    }

    @Override
    public void removeLines(List<String> lines) {

        this.lines.removeAll(lines);
        invalidate();
    }

}

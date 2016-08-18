
package de.unratedfilms.guilib.widgets.view.adapters;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import de.unratedfilms.guilib.core.WidgetAdapter;
import de.unratedfilms.guilib.widgets.model.TextTooltip;

/**
 * A minimal implementation of {@link TextTooltip} that doesn't contain any drawing code.
 */
public abstract class TextTooltipAdapter extends WidgetAdapter implements TextTooltip {

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

        super(0, 0);
        zLevel = 1.0f;

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
        updateSize();
    }

    @Override
    public void addLines(List<String> lines) {

        Validate.noNullElements(lines, "No tooltip line is allowed to be null");

        this.lines.addAll(lines);
        updateSize();
    }

    @Override
    public void removeLines(List<String> lines) {

        this.lines.removeAll(lines);
        updateSize();
    }

    /**
     * Changes the {@link #setSize(int, int) size} of this tooltip widget depending on the {@link #getLines() lines}.
     */
    protected abstract void updateSize();

}

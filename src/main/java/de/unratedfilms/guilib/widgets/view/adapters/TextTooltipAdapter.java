
package de.unratedfilms.guilib.widgets.view.adapters;

import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.TextTooltip;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * A minimal implementation of {@link TextTooltip} that doesn't contain any drawing code.
 */
public abstract class TextTooltipAdapter extends ContextHelperWidgetAdapter implements TextTooltip {

    private final List<String> lines = new ArrayList<>();

    public TextTooltipAdapter(List<String> lines) {

        setLines(lines);
    }

    @Override
    public List<String> getLines() {

        return lines;
    }

    @Override
    public TextTooltipAdapter setLines(List<String> lines) {

        Validate.noNullElements(lines, "No tooltip line is allowed to be null");

        this.lines.clear();
        this.lines.addAll(lines);
        invalidate();

        return this;
    }

    @Override
    public TextTooltipAdapter addLines(List<String> lines) {

        Validate.noNullElements(lines, "No tooltip line is allowed to be null");

        this.lines.addAll(lines);
        invalidate();

        return this;
    }

    @Override
    public TextTooltipAdapter removeLines(List<String> lines) {

        this.lines.removeAll(lines);
        invalidate();

        return this;
    }

}


package de.unratedfilms.guilib.widgets.view.adapters;

import org.apache.commons.lang3.Validate;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.Label;

/**
 * A minimal implementation of {@link Label} that doesn't contain any drawing code.
 */
public abstract class LabelAdapter extends ContextHelperWidgetAdapter implements Label {

    private String text;

    public LabelAdapter(String text) {

        setText(text);
    }

    @Override
    public String getText() {

        return text;
    }

    @Override
    public void setText(String text) {

        Validate.notNull(text, "Label text cannot be null");
        this.text = text;
        invalidate();
    }

}

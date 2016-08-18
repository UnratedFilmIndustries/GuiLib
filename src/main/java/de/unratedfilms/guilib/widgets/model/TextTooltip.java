
package de.unratedfilms.guilib.widgets.model;

import java.util.List;
import de.unratedfilms.guilib.core.Widget;

/**
 * Abstract representation of a tooltip which displays multiple lines of text.
 */
public interface TextTooltip extends Widget {

    public List<String> getLines();

    public void setLines(List<String> lines);

    public void addLines(List<String> lines);

    public void removeLines(List<String> lines);

}

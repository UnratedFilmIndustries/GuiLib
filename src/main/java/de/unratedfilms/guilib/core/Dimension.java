
package de.unratedfilms.guilib.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An immutable object which represents a certain width and height, without actually being located at a specific point.
 */
public class Dimension {

    private final int width, height;

    public Dimension(int width, int height) {

        Validate.isTrue(width >= 0, "Width of dimension object must not be negative");
        Validate.isTrue(height >= 0, "Height of dimension object must not be negative");

        this.width = width;
        this.height = height;
    }

    public int getWidth() {

        return width;
    }

    public Dimension withWidth(int newWidth) {

        return new Dimension(newWidth, height);
    }

    public int getHeight() {

        return height;
    }

    public Dimension withHeight(int newHeight) {

        return new Dimension(width, newHeight);
    }

    @Override
    public int hashCode() {

        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {

        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {

        return "(w=" + width + "|h=" + height + ")";
    }

}

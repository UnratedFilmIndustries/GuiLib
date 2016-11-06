
package de.unratedfilms.guilib.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An immutable 2D point with x/y coordinates.
 */
public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {

        this.x = x;
        this.y = y;
    }

    public int getX() {

        return x;
    }

    public Point withX(int newX) {

        return new Point(newX, y);
    }

    public int getY() {

        return y;
    }

    public Point withY(int newY) {

        return new Point(x, newY);
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

        return "(" + x + "|" + y + ")";
    }

}


package de.unratedfilms.guilib.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An immutable 2D rectangle with an x/y upper left corner (position) and a size (width/height).
 */
public class Rectangle {

    private final Point     position;
    private final Dimension size;

    public Rectangle(int x, int y, int width, int height) {

        this(new Point(x, y), new Dimension(width, height));
    }

    public Rectangle(Point position, int width, int height) {

        this(position, new Dimension(width, height));
    }

    public Rectangle(int x, int y, Dimension size) {

        this(new Point(x, y), size);
    }

    public Rectangle(Point position, Dimension size) {

        Validate.notNull(position, "Position of rectangle must not be null");
        Validate.notNull(size, "Size of rectangle must not be null");

        this.position = position;
        this.size = size;
    }

    public Rectangle(org.lwjgl.util.Rectangle lwjglRectangle) {

        this(lwjglRectangle.getX(), lwjglRectangle.getY(), lwjglRectangle.getWidth(), lwjglRectangle.getHeight());
    }

    public int getX() {

        return position.getX();
    }

    public Rectangle withX(int newX) {

        return withPosition(position.withX(newX));
    }

    public int getY() {

        return position.getY();
    }

    public Rectangle withY(int newY) {

        return withPosition(position.withY(newY));
    }

    public Point getPosition() {

        return position;
    }

    public Rectangle withPosition(int newX, int newY) {

        return withPosition(new Point(newX, newY));
    }

    public Rectangle withPosition(Point newPosition) {

        return new Rectangle(newPosition, size);
    }

    public int getWidth() {

        return size.getWidth();
    }

    public Rectangle withWidth(int newWidth) {

        return withSize(size.withWidth(newWidth));
    }

    public int getHeight() {

        return size.getHeight();
    }

    public Rectangle withHeight(int newHeight) {

        return withSize(size.withHeight(newHeight));
    }

    public Dimension getSize() {

        return size;
    }

    public Rectangle withSize(int newWidth, int newHeight) {

        return withSize(new Dimension(newWidth, newHeight));
    }

    public Rectangle withSize(Dimension newSize) {

        return new Rectangle(position, newSize);
    }

    public boolean inBounds(int x, int y) {

        return x >= getX() && y >= getY() & x < getX() + getWidth() && y < getY() + getHeight();
    }

    public Rectangle intersection(Rectangle other) {

        // We cheat and just use the intersection code from LWJGL
        org.lwjgl.util.Rectangle dest = new org.lwjgl.util.Rectangle();
        toLwjgl().intersection(other.toLwjgl(), dest);

        // If the size of the new rectangle is negative, the two rectangles don't have an intersection.
        // Since we can't have rectangles with negative size, the new rectangle is given a size of 0.
        dest.setWidth(Math.max(dest.getWidth(), 0));
        dest.setHeight(Math.max(dest.getHeight(), 0));

        return new Rectangle(dest);
    }

    public org.lwjgl.util.Rectangle toLwjgl() {

        return new org.lwjgl.util.Rectangle(getX(), getY(), getWidth(), getHeight());
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

        return "(x=" + getX() + ",y=" + getY() + "|w=" + getWidth() + ",h=" + getHeight() + ")";
    }

}

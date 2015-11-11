package model;

/**
 * Class that represents a single point in Cartesian coordinates
 * @author Kairi Kozuma
 * @version 1.0
 */
public class Point {
    // x and y values of point, with index
    private int x;
    private int y;
    private int index;

    /**
     * Constructs point
     * @param   x0 x value
     * @param   y0 y value
     */
    public Point(int x0, int y0) {
        this(x0, y0, 0);
    }

    /**
     * Constructs point
     * @param   x0 x value
     * @param   y0 y value
     * @param   i0 index value
     */
    public Point(int x0, int y0, int i0) {
        x = x0;
        y = y0;
        index = i0;
    }

    /**
     * Return the x value of the point
     * @return x value of point
     */
    public int getX() {
        return x;
    }

    /**
     * Return the y value of the point
     * @return y value of point
     */
    public int getY() {
        return  y;
    }

    /**
     * Return the index label of the point
     * @return index value of point
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the x value of the point
     * @param int x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y value of the point
     * @param int y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the index of the point
     * @param int index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Return string representation of point
     * @return x and y values of point
     */
    @Override
    public String toString() {
        return String.format("%2d (%d,%d)", index, x, y);
    }

    /**
     * Checks if points are equal
     * @return boolean if points are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {return false;}
        if (this == null) {return true;}
        if (!(o instanceof Point)) {return false;}
        Point p = (Point) o;
        return this.getX() == p.getX() && this.getY() == p.getY();
    }

    /**
     * Hashcode for points
     * @return int Hashcode
     */
    @Override
    public int hashCode() {
        return x * 17 + y * 31;
    }
}

package model;

/**
 * Class that represents a real point in Cartesian coordinates
 * @author Kairi Kozuma
 * @version 1.0
 */
public class PointReal {
    // x and y values of point, with index
    private double x;
    private double y;
    private int index;
    private double error;

    /**
     * Constructs point with real coordinates
     * @param   x0 x value
     * @param   y0 y value
     */
    public PointReal(double x0, double y0) {
        this(x0, y0, 0);
    }

    /**
     * Constructs point with real coordinates
     * @param   x0 x value
     * @param   y0 y value
     * @param   i0 index value
     */
    public PointReal(double x0, double y0, int i0) {
        x = x0;
        y = y0;
        index = i0;
        error = 0.0;

        if (index != -1) {
            // Calculate error to closest integer point
            int x_close = (int) Math.round(x);
            int y_close = (int) Math.round(y);
            error = Math.sqrt(Math.pow(x_close - x, 2) + Math.pow(y_close - y, 2));
        }
    }

    /**
     * Return the x value of the point
     * @return x value of point
     */
    public double getX() {
        return x;
    }

    /**
     * Return the y value of the point
     * @return y value of point
     */
    public double getY() {
        return y;
    }

    /**
     * Return the index label of the point
     * @return index value of point
     */
    public int getIndex() {
        return index;
    }

    /**
     * Return the error of the point
     * @return error value of point
     */
    public double getError() {
        return error;
    }

    /**
     * Sets the x value of the point
     * @param int x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the y value of the point
     * @param int y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Sets the index of the point
     * @param int index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    public Point nearest() {
        return new Point((int) Math.round(x), (int) Math.round(y), index);
    }

    /**
     * Return string representation of point
     * @return x and y values of point
     */
    @Override
    public String toString() {
        return String.format("%2d (%.3f,%.3f) E: %.3f ", index, x, y, error);
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
        PointReal p = (PointReal) o;
        return Math.abs(this.getX() - p.getX()) < 1e-6
            && Math.abs(this.getY() - p.getY()) < 1e-6;
    }

    /**
     * Hashcode for points
     * @return int Hashcode
     */
    @Override
    public int hashCode() {
        return (int) x * 17 + (int) y * 31;
    }
}

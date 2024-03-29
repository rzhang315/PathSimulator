package model;

/**
 * Class that represents a line segment
 * @author Kairi Kozuma
 * @version 1.1
 */
public class LineSegment {

    // Constants
    public static final LineSegment X_UNIT = new LineSegment(new Point(0, 0), new Point(1,0));
    public static final LineSegment Y_UNIT = new LineSegment(new Point(0, 0), new Point(0,1));

    // Points in line segment
    private Point p1;
    private Point p2;

    // Constructor
    public LineSegment(Point start, Point end) {
        p1 = start;
        p2 = end;
    }

    /**
     * Return the x component of the line segment
     * @return x component
     */
    public int getDX() {
        return p2.getX() - p1.getX();
    }

    /**
     * Return the y component of the line segment
     * @return y component
     */
    public int getDY() {
        return p2.getY() - p1.getY();
    }

    /**
     * Return the first point of the line segment
     * @return Point first point
     */
    public Point getStart() {
        return p1;
    }

    /**
     * Return the second point of line segment
     * @return Point second point
     */
    public Point getEnd() {
        return p2;
    }

    /**
     * Get the length of the line segment
     * @return length of the segment
     */
    public double length() {
        return Math.sqrt(
            Math.pow(getDX(), 2.0)
          + Math.pow(getDY(), 2.0));
    }

    /**
     * Returns the angle between the two line segments
     * @param  s another line segment
     * @return   angle between the line segments in degrees
     */
    public double angle(LineSegment s) {
        return Math.toDegrees(Math.acos(
            ((this.getDX() * s.getDX()) + (this.getDY() * s.getDY()))
            / (this.length() * s.length())));
    }

    /**
     * Return string representation of line segment
     * @return points and length
     */
    @Override
    public String toString() {
        return String.format("%s -> %s Length: %.3f\n", p1, p2, length());
    }
}

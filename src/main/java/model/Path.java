package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Class that represents a path with list of line segments
 * @author Kairi Kozuma
 * @version 1.0
 */

public class Path implements Comparable<Path> {
    // List of line segments that make line
    List<LineSegment> mSegments = new ArrayList<LineSegment>();
    List<Point> mPoints = new ArrayList<Point>();

    // Angle needed to turn to first point
    double firstTurnAngle = 0.0;

    // Initial point
    public static final Point originPoint = new Point(0, 0, 0);

    /**
     * Construct new Path from list of points
     * @param  pList list of points
     */
    public Path(List<Point> pList) {
        List<Point> mPList = new ArrayList<Point>(pList);

        mPoints = mPList;
        for (int i = 0; i < pList.size() - 1; i++) {
            mSegments.add(new LineSegment(pList.get(i), pList.get(i + 1)));
        }
        firstTurnAngle = (new LineSegment(new Point(0,0), new Point(1,0)))
            .angle(mSegments.get(0));
    }

    /**
     * Construct new Path from list of line segments
     * @param  lineSegList list of line segments
     */
    public Path(List<LineSegment> lineSegList, boolean useSegments) {
        mSegments = lineSegList;
        firstTurnAngle = 0; // TODO: Fix initial angle
    }

    /**
     * Total length of the path
     * @return length of the path
     */
    public double length() {
        double totalLength = 0.0;
        for (LineSegment s : mSegments) {
            totalLength += s.length();
        }
        return totalLength;
    }

    /**
     * Total angle turned during path
     * @return angle turned during path
     */
    public double angle() {
        double totalAngle = 0.0;
        for (int i = 0; i < mSegments.size() - 1; i++) {
            totalAngle += Math.abs(mSegments.get(i).angle(mSegments.get(i+1)));
        }
        return totalAngle + firstTurnAngle;
    }

    /**
     * Total angle turned during path, with the smallest angle calculated
     * @return total of smallest angle turns
     */
    public double angleSmallest() {
        double totalAngle = 0.0;
        for (int i = 0; i < mSegments.size() - 1; i++) {
            double angle = Math.abs(mSegments.get(i).angle(mSegments.get(i+1)));
            angle = angle > 90 ? 180 - angle : angle;
            totalAngle += angle;
        }
        return totalAngle
            + (firstTurnAngle > 90 ? 180 - firstTurnAngle : firstTurnAngle);
    }

    /**
     * Returns points that comprise the path
     * @return points of the path
     */
    public List<Point> getPoints() {
         return mPoints;
    }

    /**
     * Returns the line segments that comprise the path
     * @return list of line segments
     */
    public List<LineSegment> getLineSegments() {
        return mSegments;
    }

    /**
     * String representation of path
     * @return String with total distance and angle turned
     */
    @Override
    public String toString() {
        return String.format("Length: %.3f\nForward Angle: %.3f"
          + "\nSmallest Angle: %.3f\n" + mSegments, length(), angle(), angleSmallest());
    }

    @Override
    public int compareTo(Path p) {
        return (int) Math.ceil(this.length() - p.length());
    }
}

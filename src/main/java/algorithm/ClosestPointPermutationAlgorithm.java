package algorithm;

import model.Path;
import model.Point;
import model.LineSegment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.Collections;
import util.MathUtil;
/**
 * Closest point permutation algorithm (hybrid)
 * @author Kairi Kozuma
 * @version 1.0
 */
public class ClosestPointPermutationAlgorithm extends PermutationAlgorithm {

    private int branch;
    private int iteration;

    /**
     * Construct ClosestPointPermutationAlgorithm object from list of points
     * @param  listPoints List of points that the algorithm operates on
     * @param  branch number of branches to branch out from starting point and each subsequent point
     * @param  number of iterations to continue branching
     */
    public ClosestPointPermutationAlgorithm(List<Point> listPoints, int branch, int iteration) {
        super(listPoints);
        numPath = (long) Math.pow(branch, iteration) * MathUtil.factorial(listPoints.size() - iteration);
        this.branch = branch;
        this.iteration = iteration;
    }


    // TODO: NOT use line segment, make point to point length function, maybe remove Line Segment altogether

    @Override
    public Path bestPath() {
        closestPointThenPermutate();
        return shortestPath;
    }

    private void closestPointThenPermutate() {

        Point origin = new Point(0,0,0);
        List<Point> head = new ArrayList<Point>();
        head.add(origin);
        closestPointPerm(head, listOfPoints);
    }

    private void closestPointPerm(List<Point> head, List<Point> end) {
        if (head.size() == iteration + 1) {
            head.remove(0); // Remove the origin from permutation
            permutation(head, end);
        } else {
            int numPointCheck = branch < end.size() ? branch : end.size();

            List<LineSegment> mSegments = new ArrayList<LineSegment>(end.size());
            Point p = head.get(head.size() - 1);
            for (Point pnt : end) {
                mSegments.add(new LineSegment(p, pnt));
            }

            Collections.sort(mSegments, Comparator.comparing(LineSegment::length));
            List<Point> closePoints = new ArrayList<Point>(numPointCheck);
            for (int i = 0; i < numPointCheck; i++) {
                closePoints.add(mSegments.get(i).getEnd());
            }

            for (Point pNext : closePoints) {
                List<Point> mHead = new ArrayList<Point>(head);
                List<Point> mEnd = new ArrayList<Point>(end);
                mHead.add(pNext);
                mEnd.remove(pNext);
                closestPointPerm(mHead, mEnd);
            }
        }
    }
}

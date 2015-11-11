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
        this.branch = branch;
        this.iteration = iteration;
    }


    // TODO: NOT use line segment, make point to point length function, maybe remove Line Segment altogether

    @Override
    public Path bestPath() {
        closestPointThenPermutate();
        return getPath();
    }

    private void closestPointThenPermutate() {

        Point origin = new Point(0,0,0);
        List<Point> head = new ArrayList<Point>();
        head.add(origin);
        closestPointPerm(head, getListOfPoints(), branch, iteration);
    }

    private void closestPointPerm(List<Point> head, List<Point> end, int numPoints, int numIteration) {
        if (numPoints > end.size()) {
            return;
        }

        if (head.size() == numIteration + 1) {
            head.remove(0); // Remove the origin from permutation
            permutation(head, end);
        }

        List<LineSegment> mSegments = new ArrayList<LineSegment>();
        Point p = head.get(head.size() - 1);
        for (Point pnt : end) {
            mSegments.add(new LineSegment(p, pnt));
        }

        Collections.sort(mSegments, Comparator.comparing(LineSegment::length));
        List<Point> closePoints = new ArrayList<Point>(numPoints);
        for (int i = 0; i < numPoints; i++) {
            closePoints.add(mSegments.get(i).getEnd());
        }

        for (Point pNext : closePoints) {
            List<Point> mHead = new ArrayList<Point>(head);
            List<Point> mEnd = new ArrayList<Point>(end);
            mHead.add(pNext);
            mEnd.remove(pNext);
            closestPointPerm(mHead, mEnd, branch, numIteration - 1);
        }
    }
}

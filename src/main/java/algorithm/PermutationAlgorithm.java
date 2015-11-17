package algorithm;

import model.Path;
import model.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;
import util.MathUtil;

/**
 * Permutation algorithm (brute force)
 * @author Kairi Kozuma
 * @version 1.1
 */
public class PermutationAlgorithm extends SortAlgorithm {

    /**
     * Construct PermutationAlgorithm object from list of points
     * @param  listPoints List of points that the algorithm operates on
     */
    public PermutationAlgorithm(List<Point> listPoints) {
        super(listPoints);
        numPath = MathUtil.factorial(listPoints.size());
    }

    @Override
    public Path bestPath() {
        permutate();
        return getShortestPath();
    }

    protected Path getShortestPath() {
        // Check if origin is first point, include it if it isn't
        // in the case if path is already the shortest
        if (!shortestPath.getPoints().get(0).equals(Point.ORIGIN)) {
            shortestPath.getPoints().add(0, Point.ORIGIN);
        }
        return shortestPath;
    }

    protected void permutate() {
        permutation(new ArrayList<Point>(), listOfPoints);
    }

    protected void permutation(List<Point> mPointsBegin, List<Point> mPointsEnd) {
        int n = mPointsEnd.size();
        if (n == 0) {
            List<Point> mPoints = new ArrayList<Point>(mPointsBegin);
            mPoints.add(0, Point.ORIGIN);
            Path mPath = new Path(mPoints);
            double pathLength = mPath.length();
            if (shortestPathLength > pathLength) {
                shortestPath = mPath;
                shortestPathLength = pathLength;
            }
        } else {
            for (int i = 0; i < n; i++) {
                List<Point> mPE = new ArrayList<Point>(mPointsEnd);
                List<Point> mPB = new ArrayList<Point>(mPointsBegin);
                mPB.add(mPE.remove(i));
                permutation(mPB, mPE);
            }
        }
    }
}

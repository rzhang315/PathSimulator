import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;

/**
 * Sort a list of points for maximum efficiency
 * @author Kairi Kozuma
 * @version 1.0
 */
public class PointSorter {

    // Boundaries for points
    private static final int X_MAX = 4;
    private static final int X_MIN = -4;
    private static final int Y_MAX = 5;
    private static final int Y_MIN = -5;

    private static double minLength = 0.0;

    // Check if points are within bounds
    private static boolean withinBounds(int x, int y) {
        return (x >= X_MIN && x <= X_MAX) && (y >= Y_MIN && y <= Y_MAX);
    }

    // List of points to sort
    private List<Point> mPoints;

    // Private list of paths
    private List<Path> mPathList = new ArrayList<Path>();

    // Constructor
    public PointSorter(List<Point> listPoints) {
        mPoints = listPoints;
        minLength = (new Path(mPoints)).length();
    }

    public Path best() {
        permutate();
        mPathList.sort(Comparator.comparing(Path::length));
        return mPathList.get(0);
    }

    private void permutate() {
        permutation(new ArrayList<Point>(), mPoints);
    }

    private void permutation(List<Point> mPointsBegin, List<Point> mPointsEnd) {
        int n = mPointsEnd.size();
        if (n == 0) {
            List<Point> mPoints = new ArrayList<Point>(mPointsBegin);
            mPoints.add(0, Path.originPoint);
            Path mPath = new Path(mPoints);
            mPathList.add(mPath);
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

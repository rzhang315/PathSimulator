package algorithm;

import model.Path;
import model.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;

/**
 * Permutation algorithm (brute force)
 * @author Kairi Kozuma
 * @version 1.0
 */
public class PermutationAlgorithm implements SortAlgorithm {

    // List of points to sort
    private List<Point> mPoints;

    // Private list of paths
    private List<Path> mPathList;

    // Constructor
    public PermutationAlgorithm(List<Point> listPoints) {
        mPoints = listPoints;
        mPathList = new ArrayList<Path>();
    }

    @Override
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

package algorithm;

import model.Path;
import model.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;

/**
 * Sort Algorithm interface
 * @author Kairi Kozuma
 * @version 1.0
 */
public abstract class SortAlgorithm {

    protected List<Point> listOfPoints;
    protected Path shortestPath;
    protected double shortestPathLength;
    protected long numPath;

    /**
     * Create a sort algorithm
     * @param  listPoints List of points that the algorithm operates on
     */
    public SortAlgorithm(List<Point> listPoints) {
        listOfPoints = listPoints;
        shortestPath = new Path(listOfPoints);
        shortestPathLength = shortestPath.length();
        numPath = 0;
    }

    /**
     * Return best path from the points
     * @return Path best path
     */
    public abstract Path bestPath();

    /**
     * Getter for list of points the algorithm operates on
     * @return mPoints list of points
     */
    public List<Point> getListOfPoints() {
        return listOfPoints;
    }

    /**
     * Get number of paths checked
     * @return numPath number of paths checked in algorithm
     */
    public long getNumPath() {
        return numPath;
    }
}

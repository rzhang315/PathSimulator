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

    private List<Point> listOfPoints;
    private Path shortestPath;

    /**
     * Create a sort algorithm
     * @param  listPoints List of points that the algorithm operates on
     */
    public SortAlgorithm(List<Point> listPoints) {
        listOfPoints = listPoints;
        shortestPath = new Path(listOfPoints);
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
     * Set the shortest path
     * @param path Path to be set as shortest
     */
    protected void setPath(Path path) {
        shortestPath = path;
    }

    /**
     * Get the shortestPath
     * @return Path shortestPath
     */
    protected Path getPath() {
        return shortestPath;
    }
}

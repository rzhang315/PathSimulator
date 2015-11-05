import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Class that generates random unique points that are not 0,0
 * @author Kairi Kozuma
 * @version 1.0
 */
public class PointGenerator {

    public static void main(String[] args) {
        List<Point> mPointList = generate(8, -4, 4, -5, 5);

        String fileName;
        if (args.length == 0) {
            fileName = "pointsRandom.txt";
        } else {
            fileName = args[0];
        }
        writeToFile(mPointList, fileName);
    }

    public static List<Point> generate(int numberOfPoints,
        final int X_MIN, final int X_MAX, final int Y_MIN, final int Y_MAX) {

        // Use set to avoid duplicate points
        Set<Point> mPoints = new HashSet<Point>();

        int deltaX = X_MAX - X_MIN;
        int deltaY = Y_MAX - Y_MIN;

        while (mPoints.size() < numberOfPoints) {

            // Initialize x and y variables
            int x = 0;
            int y = 0;

            // Generate points until x and y are not both 0
            while (x == 0 && y == 0) {
                // Create random point within bounds
                x = (int) Math.floor((deltaX + 1) * Math.random()) + X_MIN;
                y = (int) Math.floor((deltaY + 1) * Math.random()) + Y_MIN;
            }

            mPoints.add(new Point(x, y, mPoints.size() + 1));
        }

        return new ArrayList<Point>(mPoints);
    }

    public static void writeToFile(List<Point> pointList, String fileName) {
        try {
            PrintStream mFileStream = new PrintStream(new File(fileName));
            for (Point p : pointList) {
                mFileStream.println(String.format("%d, %d, %d", p.getX(), p.getY(), p.getIndex()));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }
}

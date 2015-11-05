import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Parse a path from a text file
 * @author Kairi Kozuma
 * @version 1.0
 */
public class PathParser {
    /**
     * Parse a single path from a filename
     * @param  fileName String file name
     * @return          Path object parsed from the file
     */
    public static Path parsePathFromFile(String fileName) {
        List<Point> pointList = new ArrayList<Point>();
        try {
            Scanner mFileScanner = new Scanner(new File(fileName));
            while(mFileScanner.hasNext()) {
                int[] point = parseLine(mFileScanner.nextLine());
                pointList.add(new Point(point[0], point[1], point[2]));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return null;
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }
        return new Path(pointList);
    }

    /**
     * Parse multiple paths from multiple filenames
     * @param  fileNameArray array of filenames
     * @return               Map of paths parsed from file
     */
    public static Map<Path, String> parseMultiplePathFromFile(String[] fileNameArray) {

        List<Point> pointList = new ArrayList<Point>();
        Map<Path, String> pathMap = new TreeMap<Path, String>();
        List<String> fileNames = new ArrayList<String>();

        // Add filenames to list
        if (fileNameArray == null) {
            fileNames.add("points.txt");
        } else {
            for (int i = 0; i < fileNameArray.length; i++) {
                fileNames.add(fileNameArray[i]);
            }
        }

        // Parse each text file
        for (String mName : fileNames) {
            pointList.clear();
            try {
                Scanner mFileScanner = new Scanner(new File(mName));
                while(mFileScanner.hasNext()) {
                    int[] point = parseLine(mFileScanner.nextLine());
                    pointList.add(new Point(point[0], point[1], point[2]));
                }
            } catch (FileNotFoundException e) {
                System.out.println(e);
                return null;
            } catch (NumberFormatException e) {
                System.out.println(e);
                return null;
            }
            pathMap.put(new Path(pointList), mName);
        }

        return pathMap;
    }

    // Parse the line and return the integer values
    private static int[] parseLine(String line) throws NumberFormatException {
        String[] tokens = line.split("[, ]+");
        int[] point = new int[tokens.length];
        for (int i = 0; i < point.length; i++) {
            point[i] = Integer.parseInt(tokens[i]);
        }
        return point;
    }
}

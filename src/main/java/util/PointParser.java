package util;

import model.Path;
import model.Point;
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
public class PointParser {
    /**
     * Parse a list of points from string
     * @param  rawString String of point list to parse
     * @return           List<Point> parsed from String
     */
    public static List<Point> parsePointsFromString(String rawString) {
        List<Point> pointList = new ArrayList<Point>();
        try {
            int i = 1;
            for (String line : rawString.split("\\n")) {
                int[] pointData = parseLine(line);
                if (pointData.length == 2) {
                    pointList.add(new Point(pointData[0], pointData[1], i));
                } else if (pointData.length == 3) {
                    pointList.add(new Point(pointData[0], pointData[1], pointData[2]));
                } else {
                    // throw exception
                }
                i++;
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }
        return pointList;
    }

    /**
     * Parse a single path from a filename
     * @param  fileName String file name
     * @return          Path object parsed from the file
     */
    public static List<Point> parsePoingFromFile(String fileName) {
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
        return pointList;
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

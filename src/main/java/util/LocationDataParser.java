package util;

import model.Path;
import model.Point;
import model.PointReal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Parse data from raw location data
 * @author Kairi Kozuma
 * @version 1.0
 */
public class LocationDataParser {

    // Conversion factor from robot units to feet
    private static final double CONVERSION_FACTOR = 1 / 290.29;

    /**
     * Parse experimental data from string text
     * @param  dataString String of data to parse
     * @return List<Point> list of points
     */
    public static List<PointReal> parseDataFromString(String dataString, boolean getAllPoints) {
        List<PointReal> pointList = new ArrayList<PointReal>();
        try {
            for (String line : dataString.split("\\n")) {
                PointReal point = parseLine(line);
                if (getAllPoints || point.getIndex() != -1) {
                    pointList.add(point);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }
        return pointList;
    }

    /**
     * Parse experimental data from raw location file
     * @param  fileName String file name
     * @return List<PointReal> list of points
     */
    public static List<PointReal> parseDataFromFile(String fileName, boolean getAllPoints) {
        List<PointReal> pointList = new ArrayList<PointReal>();
        try {
            Scanner mFileScanner = new Scanner(new File(fileName));
            while(mFileScanner.hasNext()) {
                PointReal point = parseLine(mFileScanner.nextLine());
                if (getAllPoints || point.getIndex() != -1) {
                    pointList.add(point);
                }
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
     * Parse a line of raw data and return a PointReal
     * @param  line of String to parse
     * @return      PointReal of location if valid
     */
    private static PointReal parseLine(String line) throws NumberFormatException {
        // Separate the string into substrings
        String xStr = line.substring(0,4);
        String yStr = line.substring(4,8);

        // Cast to short to get correct negative values
        short xval = (short) Integer.parseInt(xStr, 16);
        double xvalFeet = xval * CONVERSION_FACTOR;

        short yval = (short) Integer.parseInt(yStr, 16);
        double yvalFeet = yval * CONVERSION_FACTOR;
        int index = -1;

        // If line is length 10, designated as destination
        if (line.length() == 10) {
            String indStr = line.substring(8,10);
            index = Integer.parseInt(indStr, 16);
        }

        return new PointReal(xvalFeet, yvalFeet, index);
    }
}

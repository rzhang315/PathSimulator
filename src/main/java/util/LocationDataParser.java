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
    public static List<PointReal> parseDataFromString(String dataString) {
        List<PointReal> pointList = new ArrayList<PointReal>();
        try {
            for (String line : dataString.split("\\n")) {
                PointReal point = parseLine(line);
                if (point != null) {
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
    public static List<PointReal> parseDataFromFile(String fileName) {
        List<PointReal> pointList = new ArrayList<PointReal>();
        try {
            Scanner mFileScanner = new Scanner(new File(fileName));
            while(mFileScanner.hasNext()) {
                PointReal point = parseLine(mFileScanner.nextLine());
                if (point != null) {
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
        // If line does not have length 10, robot did not declare destination
        if (line.length() != 10) {
            return null;
        } else {
            // Separate the string into substrings
            String xStr = line.substring(0,4);
            String yStr = line.substring(4,8);
            String indStr = line.substring(8,10);

            // Cast to short to get correct negative values
            short xval = (short) Integer.parseInt(xStr, 16);
            double xvalFeet = xval * CONVERSION_FACTOR;

            short yval = (short) Integer.parseInt(yStr, 16);
            double yvalFeet = yval * CONVERSION_FACTOR;

            // Index can be left unsigned as it is always nonnegative
            int index = Integer.parseInt(indStr, 16);

            return new PointReal(xvalFeet, yvalFeet, index);
        }
    }
}

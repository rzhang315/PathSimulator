package util;

import model.PointReal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.io.PrintStream;
import java.io.File;
import java.io.FileNotFoundException;

public class PointExporter {
    // TODO: use interface that Point and PointReal extends

    public static void export(String fileName, List<PointReal> list) {

        String timeStr = LocalDateTime.now().format( DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        timeStr = timeStr.substring(0, timeStr.length() - 4);
        timeStr = timeStr.replace(":", "~");
        try {
            PrintStream fileStream = new PrintStream(new File (fileName + timeStr + ".txt"));
            for (PointReal p : list) {
                if (p.getIndex() != -1) {
                    fileStream.println(p);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }



        //         // for (Object o : list) {
        //     if (o instanceof PointReal) {
        //         PointReal p = (PointReal) o;
        //         if (p.getIndex() != -1) {
        //             fileStream.println(p);
        //         }
        //     } else {
        //         fileStream.println(o);
        //     }
        // }
    }
}

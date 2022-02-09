package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringFormatter {

    public static final SimpleDateFormat SDF_14 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final Logger LOGGER = LogManager.getLogger(StringFormatter.class);

    public static String msToHumanTime(Long millis) {
        if (millis != null) {
            long h = millis / (1000L * 60L * 60L);
            long m = millis / (1000L * 60L) - h * 60L;
            long s = millis / 1000L - h * 60L * 60L - m * 60L;
            long ms = millis - h * 1000L * 60L * 60L - m * 1000L * 60L - s * 1000L;
            String hS = h > 0 ? h + " h " : "";
            String mS = (h > 0) || (m > 0) ? m + " m " : "";
            String sS = ((h > 0) || (m > 0)) || (s > 0) ? s + " s " : "";
            String msS = (((h > 0) || (m > 0)) || (s > 0)) || (ms > 0) ? ms + " ms " : "";
            return hS + mS + sS + msS;
        }
        return null;
    }

    static String simpleDateForDirName(Date date) {
        return new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(date);
    }

    public static String byleLengthToGbMbKbString(long bytes) {
        long l = 1024L;
        long gB = bytes / (l * l * l);
        long mB = bytes / (l * l) - gB * l;
        long kB = bytes / (l) - gB * l * l - mB * l;
        long b = bytes % (l);
        String gbS = gB > 0 ? gB + " Gb " : "";
        String mbS = (gB > 0) || (mB > 0) ? mB + " Mb " : "";
        String kbS = ((gB > 0) || (mB > 0)) || (kB > 0) ? kB + " Kb " : "";
        String bS = (((gB > 0) || (mB > 0)) || (kB > 0)) || (b > 0) ? b + " b " : "";
        return gbS + mbS + kbS + bS;
    }

    public static String elapsed(Date startDate) {
        if (startDate != null)
            return msToHumanTime(new Date().getTime() - startDate.getTime());
        else return null;
    }

    public static Long elapsedMS(Date startDate) {
        return new Date().getTime() - startDate.getTime();
    }

    public static Long msToEnd(Date startDate, Long index, Long size) {
        long elapsedMS = new Date().getTime() - startDate.getTime();
        if (index == 0) index++;
        return size * elapsedMS / index - elapsedMS;
    }

    public static final Character DECIMAL_SEPARATOR = new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator();

    public static double p(long index, long size) {
        double p = ((double) index / (double) size) * 100;
        return p;
    }

    public static String getTimeToEnd(Date totalStartTime, Long i, Long size) {
        return msToHumanTime(msToEnd(totalStartTime, i, size));
    }

    public static final long MS_IN_DAY = 1000 * 60 * 60 * 24;
    public static final SimpleDateFormat SDF_YYYY_MM_DD = new SimpleDateFormat("YYY-MM-dd");
    public static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    public static String makeJSONDateString(Date date) {
        return SDF_YYYY_MM_DD.format(date) + "T" + sdfTime.format(date);
    }

    public static String makeProgressLogString(String methodName, Date startDate, int index, int size) {
        long timeToEnd = msToEnd(startDate, (long) index, (long) size);
        long elapsed = new Date().getTime() - startDate.getTime();
        if (elapsed == 0) {
            elapsed = 1;
        }
        return "[" + methodName + "]\t" +
                "[" + index + "/" + size + "]\t" +
                "[" + p(index, size) + " %]\t" +
                String.format("%-20s", "[time ot end : " + msToHumanTime(timeToEnd) + "]\t") +
                String.format("%-20s", "[estimate : " + msToHumanTime(elapsed + timeToEnd) + "]\t") +
                String.format("%-20s", "[elapsed : " + msToHumanTime(elapsed) + "]\t" +
                        String.format("%-20s", "[velocity : " + velocity(index, elapsed)) + " per second]\t");
    }

    public static String progressLog(Date start, long i, long size) {
        try {
            String e = elapsed(start);
            String t = getTimeToEnd(start, i, size);
            String p = p(i, size) + "";
            return "[elapsed : " + e + "][timeToEnd : " + t + "][progress : " + p + "%][i/size : " + i + "/" + size + "]";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String velocity(int index, long elapsed) {
        return ((double) index / ((double) elapsed)) * 1000d + "";
    }


    public static String string2url(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8");
    }
}

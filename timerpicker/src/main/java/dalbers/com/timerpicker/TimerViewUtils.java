package dalbers.com.timerpicker;

import java.util.Arrays;

/**
 * Created by davidalbers on 7/16/16.
 */
public class TimerViewUtils {
    public static final String[] hmsDelimiters = {"h ","m ","s"};
    public static final String[] punctuationDelimiters = {":",".",""};
    public enum DelimiterType {
        hms,
        punctuation
    }

    public static String millisToFormattedHMS(long millis,DelimiterType delimiterType) {
        return stringToFormattedHMS(millisToHMSZeros(millis),delimiterType);
    }
    /**
     * Convert a string with hours mins seconds and any number of characters to milliseconds
     *
     * @param time a string with variable number of numbers and chars
     * @return milliseconds
     */
    public static long timeStringToMillis(String time) {
        int[] HMS = timeStringToHMS(time);
        return hmsToMillis(HMS[0],HMS[1],HMS[2]);
    }

    /**
     * Parse hours mins seconds from a string with variable number of numbers and chars
     *
     * @param time a string with variable number of numbers and chars
     * @return array of ints where index 0 is hours, 1 is minutes, 2 is seconds
     */
    private static int[] timeStringToHMS(String time) {
        String stripped = time.replaceAll("[^\\d]", "");
        if(stripped.length() < 6)
            return new int[]{0,0,0};
        int hrs = Integer.parseInt(stripped.substring(0, 2));
        int mins = Integer.parseInt(stripped.substring(2, 4));
        int secs = Integer.parseInt(stripped.substring(4, 6));

        mins += secs / 60;
        secs = secs % 60;

        hrs += mins / 60;
        mins = mins % 60;
        return new int[]{hrs, mins, secs};
    }

    /**
     * Converts milliseconds to hours minutes and seconds
     * Where there is a maximum of two digits for each and if there is only
     * one digit, a zero is added in front
     * if millis = 60000 this would return "000100" (1 minute)
     *
     * @param millis milliseconds
     * @return string concatenated as hours+mins+seconds with zeros added appropriately
     */
    private static String millisToHMSZeros(long millis) {
        //for now, lets not try to display negative time...
        if(millis < 0L)
            millis = 0L;
        int[] HMS = millisToHMS(millis);
        String formatted = "";
        if (HMS[0] < 10)
            formatted += "0" + HMS[0];
        else formatted += HMS[0];

        if (HMS[1] < 10)
            formatted += "0" + HMS[1];
        else formatted += HMS[1];

        if (HMS[2] < 10)
            formatted += "0" + HMS[2];
        else formatted += HMS[2];

        return formatted;
    }

    /**
     * Converts milliseconds to hours minutes and seconds
     * Where time 1000 would be 1 second, time 60000 would be 1 minute etc
     *
     * @param millis milliseconds
     * @return array of ints where index 0 is hours, 1 is minutes, 2 is seconds
     */
    private static int[] millisToHMS(long millis) {
        //round up
        //android's countdown timer will not tick precisely on the millisecond
        //so for example, at 2 milliseconds, you will actually have 1997 ms
        //(additionally the last tick won't happen)
        int secs = (int) (millis / 1000.0 + .5);
        int hrs = secs / (60 * 60);
        secs = secs % (60 * 60);
        int mins = secs / (60);
        secs = secs % 60;
        return new int[]{hrs, mins, secs};
    }

    public static String stringToFormattedHMS(String input) {
        return stringToFormattedHMS(input,DelimiterType.hms);
    }

    /**
     * Given a string with a variable amount of numbers and chars
     * Format it to the hour, min, sec setupView that looks like "12h 34m 56s"
     *
     * @param input string with a variable amount of numbers and chars
     * @return formatted string that looks like "12h 34m 56s"
     */
    public static String stringToFormattedHMS(String input, DelimiterType delimiterType) {
        String[] delimiters = hmsDelimiters;
        switch (delimiterType)
        {
            case hms:
                delimiters = hmsDelimiters;
                break;
            case punctuation:
                delimiters = punctuationDelimiters;
                break;
        }
        int formattedStrLen = 6 + delimiters[0].length() +
                delimiters[1].length() + delimiters[2].length();
        //backspaced, removed the 's' but the user intended to remove last second number
        if (input.length() == formattedStrLen)
            input = input.substring(0, formattedStrLen-1);
        //remove all non numbers
        String stripped = stripNonNumbers(input);
        //remove leading zeros
        while (stripped.length() > 0) {
            if (stripped.charAt(0) == '0')
                stripped = stripped.substring(1);
            else
                break;
        }
        //any number index >5
        stripped = stripped.substring(0, Math.min(stripped.length(), 6));

        String preZeros = "";
        if (stripped.length() < 6) {
            char[] zeros = new char[6 - stripped.length()];
            Arrays.fill(zeros, '0');
            preZeros = new String(zeros);
        }
        String fullNums = preZeros + stripped;

        //setupView in 12h 34m 56s
        return fullNums.substring(0, 2) + delimiters[0]  +
                fullNums.substring(2, 4) + delimiters[1] +
                fullNums.substring(4, 6) + delimiters[2];

    }

    /**
     * Remove the last number currently displayed. Useful as a "delete" command
     * @param initialString the string displayed currently
     * @param delimiterType type of delimtier being used
     * @return the string after the last time number has been removed
     */
    public static String removeLastNumber(String initialString, DelimiterType delimiterType) {
        //get only numbers
        String stripped = stripNonNumbers(initialString);
        //remove last number
        stripped = stripped.subSequence(0,stripped.length()-1).toString();
        //reformat and return
        return stringToFormattedHMS(stripped, delimiterType);
    }

    /**
     * Remove the last number currently displayed. Useful as a "delete" command.
     * Uses HMS delimiters as a default. Use removeLastNumber() with delimiter to change this.
     * @param initialString the string displayed currently
     * @return the string after the last time number has been removed
     */
    public static String removeLastNumber(String initialString) {
        return removeLastNumber(initialString, DelimiterType.hms);
    }

    private static String stripNonNumbers(String initialString) {
        return initialString.replaceAll("[^\\d]", "");
    }

    /**
     * Given numerical reprentation of hours, minutes, and seconds,
     * convert them to milliseconds
     * @param hours
     * @param mins
     * @param secs
     * @return
     */
    private static long hmsToMillis(int hours, int mins, int secs) {
        long timeInMillis = hours * 60 * 60 * 1000 +
                mins * 60 * 1000 +
                secs * 1000;
        return timeInMillis;
    }
}

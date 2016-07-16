package dalbers.com.timerpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by davidalbers on 7/10/16.
 */
public class TimerTextView extends TextView {
    private long time = 0;

    public TimerTextView(Context context) {
        super(context);
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TimerTextView,
                0, 0);
        if(a.hasValue(R.styleable.TimerTextView_time))
            time = a.getInt(R.styleable.TimerTextView_time, 0);
        a.recycle();
        setTime(time);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    public void setTime(long timeInMillis) {
        time = timeInMillis;
        //convert milliseconds to a string of hours,min,secs with leading zeros
        String hmsString = millisToHMSZeros(timeInMillis);
        //add in h,m,s chars
        hmsString = stringToFormattedHMS(hmsString);
        setTextWithSpan(hmsString);
    }

    /**
     * make "h m s" smaller than numbers
     * @param text timer text in format ##h ##m ##s
     */
    private void setTextWithSpan(String text) {
        Spannable span = new SpannableString(text);
        span.setSpan(new RelativeSizeSpan(0.6f), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.6f), 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.6f), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setText(span);
    }

    /**
     * Put a number at the end of the timer text.
     * If text is full, this will have no effect.
     * An example of when this method should be used is
     * when a number button is pressed while creating a timer.
     * @param number number to add
     */
    public void appendNumber(int number) {
        //append a char to the string
        String formattedStr = stringToFormattedHMS(getText().toString() + number);
        //update our time variable which is in ms
        time = timeStringToMillis(formattedStr);
        //apply string formatting and set text
        setTextWithSpan(formattedStr);
    }

    /**
     * Remove the number at the end of the timer text.
     * If text is empty, this will have no effect.
     * An example of when this method should be used is
     * when the delete button is pressed while creating a timer.
     */
    public void removeLastNumber() {
        //remove the last char of the textview's text
        String formattedStr = stringToFormattedHMS(
                getText().subSequence(
                        0,getText().length()-1)
                .toString());
        //update our time variable which is in ms
        time = timeStringToMillis(formattedStr);
        //apply string formatting and set text
        setTextWithSpan(formattedStr);
    }

    public long getTime() {
        return time;
    }
    /**
     * Convert a string with hours mins seconds and any number of characters to milliseconds
     *
     * @param time a string with variable number of numbers and chars
     * @return milliseconds
     */
    private long timeStringToMillis(String time) {
        int[] HMS = timeStringToHMS(time);
        long timeInMillis = HMS[0] * 60 * 60 * 1000 +
                HMS[1] * 60 * 1000 +
                HMS[2] * 1000;
        return timeInMillis;
    }

    /**
     * Parse hours mins seconds from a string with variable number of numbers and chars
     *
     * @param time a string with variable number of numbers and chars
     * @return array of ints where index 0 is hours, 1 is minutes, 2 is seconds
     */
    private int[] timeStringToHMS(String time) {
        String stripped = time.replaceAll("[^\\d]", "");
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
    private String millisToHMSZeros(long millis) {
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
    private int[] millisToHMS(long millis) {
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

    /**
     * Given a string with a variable amount of numbers and chars
     * Format it to the hour, min, sec setupView that looks like "12h 34m 56s"
     *
     * @param input string with a variable amount of numbers and chars
     * @return formatted string that looks like "12h 34m 56s"
     */
    private String stringToFormattedHMS(String input) {
        //backspaced, removed the 's' but the user intended to remove last second number
        if (input.length() == 10)
            input = input.substring(0, 9);
        //remove all non numbers
        String stripped = input.replaceAll("[^\\d]", "");
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
        return fullNums.substring(0, 2) + "h " +
                fullNums.substring(2, 4) + "m " +
                fullNums.substring(4, 6) + "s";

    }

}

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
 *
 * TODO:
 * -colors
 */
public class TimerTextView extends TextView {
    private long time = 0;
    private TimerViewUtils.DelimiterType delimiterType = TimerViewUtils.DelimiterType.hms;
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
        String hmsString = TimerViewUtils.millisToFormattedHMS(timeInMillis,delimiterType);
        applyText(hmsString);
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
        String formattedStr = TimerViewUtils.stringToFormattedHMS(getText().toString() + number,
                delimiterType);
        //update our time variable which is in ms
        time = TimerViewUtils.timeStringToMillis(formattedStr);
        //apply string formatting and set text
        applyText(formattedStr);
    }

    /**
     * Remove the number at the end of the timer text.
     * If text is empty, this will have no effect.
     * An example of when this method should be used is
     * when the delete button is pressed while creating a timer.
     */
    public void removeLastNumber() {
        //remove the last char of the textview's text
        String formattedStr = TimerViewUtils.stringToFormattedHMS(
                getText().subSequence(
                        0,getText().length()-1)
                .toString(), delimiterType);
        //update our time variable which is in ms
        time = TimerViewUtils.timeStringToMillis(formattedStr);
        //apply string formatting and set text
        applyText(formattedStr);
    }

    private void applyText(String text) {
        switch (delimiterType) {
            case hms:
                setTextWithSpan(text);
                break;
            case punctuation:
                setText(text);
                break;
        }
    }
    public long getTime() {
        return time;
    }

    public void setDelimiterType(TimerViewUtils.DelimiterType delimiterType) {
        this.delimiterType = delimiterType;
        //reapply the text
        applyText(TimerViewUtils.stringToFormattedHMS(getText().toString(),delimiterType));
    }
}

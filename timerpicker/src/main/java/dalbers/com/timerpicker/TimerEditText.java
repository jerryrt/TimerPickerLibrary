package dalbers.com.timerpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.Arrays;

/**
 * Created by davidalbers on 7/10/16.
 */
public class TimerEditText extends EditText{
    public TimerEditText(Context context) {
        super(context);
        setupView();
    }

    public TimerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public TimerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimerEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView();
    }

    /**
     * Setup all the stuff for the view
     * we need to make it work nicely
     */
    public void setupView() {
        //handle events when text is changed
        final TextWatcher countdownTimeTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                setSelection(11);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                removeTextChangedListener(this);
                //update text
                setText(stringToFormattedHMS(s.toString()));
                Spannable span = new SpannableString(getText());
                //make "h m s" smaller than numbers
                span.setSpan(new RelativeSizeSpan(0.6f), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new RelativeSizeSpan(0.6f), 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new RelativeSizeSpan(0.6f), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //apply text size changes
                setText(span);
                addTextChangedListener(this);
            }


        };
        addTextChangedListener(countdownTimeTextWatcher);

        //On touch, only focus the view and show the keyboard
        //return true to ignore the typical response that the
        //touch would have elicited
//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                requestFocus();
//                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(TimerEditText.this, InputMethodManager.SHOW_IMPLICIT);
//                Log.d("test","onTouch");
//                return true;
//            }
//        });
        //remove blinking cursor
        setCursorVisible(false);
        //make keyboard show numbers only
        setInputType(InputType.TYPE_CLASS_NUMBER);
        //set enter button on keyboard to be the word/logo "start"
        setImeActionLabel("Start", KeyEvent.KEYCODE_ENTER);
        //do not show a full screened text view when in horizontal orientation
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
//        ((Activity)getContext()).getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    /**
     * Set the time for the view
     * @param timeInMillis time represented in milliseconds
     */
    public void setTime(long timeInMillis) {
        //convert milliseconds to a string of hours,min,secs with leading zeros
        String hmsString = millisToHMSZeros(timeInMillis);
        //add in h,m,s chars
        hmsString = stringToFormattedHMS(hmsString);
        //make "h m s" smaller than numbers
        Spannable span = new SpannableString(hmsString);
        span.setSpan(new RelativeSizeSpan(0.6f), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.6f), 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.6f), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setText(span);
    }

    /**
     * Set the time for the view
     * @param hours number of hours
     * @param mins number of minutes
     * @param secs number of seconds
     */
    public void setTime(int hours, int mins, int secs) {
        setTime(HMStoMillis(hours, mins, secs));
    }

    /**
     * Convert a string with hours mins seconds and any number of characters to milliseconds
     *
     * @param time a string with variable number of numbers and chars
     * @return milliseconds
     */
    private long timeStringToMillis(String time) {
        int[] HMS = timeStringToHMS(time);
        return HMStoMillis(HMS[0],HMS[1],HMS[2]);
    }

    /**
     * Given numerical reprentation of hours, minutes, and seconds,
     * convert them to milliseconds
     * @param hours
     * @param mins
     * @param secs
     * @return
     */
    private long HMStoMillis(int hours, int mins, int secs) {
        long timeInMillis = hours * 60 * 60 * 1000 +
                mins * 60 * 1000 +
                secs * 1000;
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

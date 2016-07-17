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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
                setText(TimerViewUtils.stringToFormattedHMS(s.toString()));
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

        //ignore long presses
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        //remove blinking cursor
        setCursorVisible(false);
        //make keyboard show numbers only
        setInputType(InputType.TYPE_CLASS_NUMBER);
        //set enter button on keyboard to be the word/logo "start"
        setImeActionLabel("Start", KeyEvent.KEYCODE_ENTER);
        //do not show a full screened text view when in horizontal orientation
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

    }

    @Override
    public void onSelectionChanged(int start, int end) {
        //don't let people highlight text
        setSelection(getText().length());
    }
    /**
     * Set the time for the view
     * @param timeInMillis time represented in milliseconds
     */
    public void setTime(long timeInMillis) {
        //convert milliseconds to a string of hours,min,secs with leading zeros
        String hmsString = TimerViewUtils.millisToFormattedHMS(timeInMillis);
        //make "h m s" smaller than numbers
        Spannable span = new SpannableString(hmsString);
        span.setSpan(new RelativeSizeSpan(0.6f), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.6f), 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.6f), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setText(span);
    }






}

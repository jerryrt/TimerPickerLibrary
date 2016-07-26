package dalbers.com.timerpickerplayground;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import dalbers.com.timerpicker.TimerPickerDialogFragment;
import dalbers.com.timerpicker.TimerPickerDialogListener;
import dalbers.com.timerpicker.TimerTextView;
import dalbers.com.timerpicker.TimerViewUtils;

public class MainActivity extends AppCompatActivity {

    /*UI variables*/
    private ImageButton createDeleteButton;
    private TimerTextView timerTextView;
    private Button startStopButton;
    private Spinner delimiterSpinner;
    /**Did the user open the dialog box and set a timer?*/
    private boolean timerCreated = false;
    /**Did the user click the start button?*/
    private boolean timerStarted = false;
    private long remainingTimerTime = 0L;

    private CountDownTimer countDownTimer;

    private static final String LOG_TAG = "dalbers/TimerPickerPlayground";

    private TimerViewUtils.DelimiterType delimiterType = TimerViewUtils.DelimiterType.hms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize UI
        createDeleteButton = (ImageButton) findViewById(R.id.create_delete_timer_button);
        createDeleteButton.setOnClickListener(createDeleteTimerClickListener);

        timerTextView = (TimerTextView)findViewById(R.id.timer_text_view);

        startStopButton = (Button)findViewById(R.id.start_stop_button);
        startStopButton.setOnClickListener(startStopTimerClickListener);
        setUITimerPaused();

        delimiterSpinner = (Spinner)findViewById(R.id.delimiterSpinner);

        //fill in spinner
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(
                this,R.array.delimiter_strings,android.R.layout.simple_selectable_list_item);
        delimiterSpinner.setAdapter(spinnerArrayAdapter);
        delimiterSpinner.setOnItemSelectedListener(spinnerSelectionListener);
    }

    private AdapterView.OnItemSelectedListener spinnerSelectionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    delimiterType = TimerViewUtils.DelimiterType.hms;
                    break;
                case 1:
                    delimiterType = TimerViewUtils.DelimiterType.punctuation;
                    break;
                default:
                    delimiterType = TimerViewUtils.DelimiterType.hms;
                    break;
            }
            timerTextView.setDelimiterType(delimiterType);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            delimiterType = TimerViewUtils.DelimiterType.hms;
            timerTextView.setDelimiterType(delimiterType);
        }
    };


    /**
     * Control starting and stopping the timer, logically and visually
     */
    private View.OnClickListener startStopTimerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(timerStarted) {
                timerStarted = false;
                setUITimerPaused();
                if(countDownTimer != null)
                    countDownTimer.cancel();

            }
            else {
                timerStarted = true;
                setUITimerStarted();
                if(remainingTimerTime != 0) {
                    createSecondTimer(remainingTimerTime);
                    countDownTimer.start();
                }
            }
        }
    };

    /**
     * Control adding a new timer, this is all visual
     */
    private View.OnClickListener createDeleteTimerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(timerCreated) {
                timerCreated = false;
                setUITimerDeleted();
                //reset time since timer was deleted
                remainingTimerTime = 0L;
                if(countDownTimer != null)
                    countDownTimer.cancel();
            }
            else {
                timerCreated = true;
                showPickerDialog();
            }
        }
    };

    /**
     * Get a callback from the dialog.
     * If "set" was clicked, setup a new timer
     * else, do nothing
     */
    private TimerPickerDialogListener dialogListener = new TimerPickerDialogListener() {
        @Override
        public void timeSet(long timeInMillis) {
            remainingTimerTime = timeInMillis;
            timerTextView.setTime(timeInMillis);
            setUINewTimer();
        }

        @Override
        public void dialogCanceled() {
            setUITimerDeleted();
        }
    };

    /**
     * Show timer text, set pic on button to an "x"
     * Should be called on timer added
     */
    private void setUINewTimer() {
        Drawable deletePic = getResources().getDrawable(R.drawable.x);
        createDeleteButton.setImageDrawable(deletePic);
        timerTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Hide timer text, set pic on button to a "+"
     * Should be called on timer deleted
     */
    private void setUINoTimer() {
        Drawable addPic = getResources().getDrawable(R.drawable.plus);
        createDeleteButton.setImageDrawable(addPic);
        timerTextView.setVisibility(View.INVISIBLE);
    }

    /**
     * Set button to have pause icon and say "Pause"
     */
    private void setUITimerStarted() {
        Resources res = MainActivity.this.getResources();
        Drawable playPic = res.getDrawable(R.drawable.pause);
        playPic.setBounds(0, 0, 120, 120);
        startStopButton.setCompoundDrawables(playPic, null, null, null);
    }

    /**
     * Set button to have play icon and say "Start"
     */
    private void setUITimerPaused() {
        Resources res = MainActivity.this.getResources();
        Drawable playPic = res.getDrawable(R.drawable.arrow);
        playPic.setBounds(0, 0, 120, 120);
        startStopButton.setCompoundDrawables(playPic, null, null, null);
        startStopButton.setText(getString(R.string.start_timer));
    }

    /**
     * Technically a combination of setUITimerPaused
     * and setUINoTimer.
     * Hide timer text, set add/delete button icon to delete
     * set play/pause button to icon to play and text to "Play"
     */
    private void setUITimerDeleted() {
        setUITimerPaused();
        setUINoTimer();
    }

    /**
     * Show picker to create a timer
     */
    private void showPickerDialog() {
        TimerPickerDialogFragment timerDialog = new TimerPickerDialogFragment();
        timerDialog.setDelimiter(delimiterType);
        timerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
        timerDialog.setDialogListener(dialogListener);

        startStopButton.setText(getString(R.string.pause_timer));
    }

    /**
     * Create a timer that counts down by seconds
     * @param time amount of time to countdown in ms
     */
    private void createSecondTimer(long time) {
        /**
         * IMPORTANT!!!
         * - CountDownTimer will not call onTick() precisely
         *      ~example the tick for 5 seconds remaining MAY happen with 4997ms remaining
         * - onTick will not be called for 0ms remaining
         * - the last onTick never happens (maybe? not 100% sure)
         *      ~so if you want a timer for seconds, it has to happen every half second
         *      ~otherwise, the last tick (for 1 second remaining) will be lost
         */
        countDownTimer = new CountDownTimer(time, 500L) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimerTime = millisUntilFinished;
                timerTextView.setTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                remainingTimerTime = 0L;
                timerTextView.setTime(0L);
                setUITimerDeleted();
            }
        };
    }
}

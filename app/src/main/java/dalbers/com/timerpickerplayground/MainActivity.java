package dalbers.com.timerpickerplayground;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import dalbers.com.timerpicker.TimerPickerDialogFragment;
import dalbers.com.timerpicker.TimerPickerDialogListener;

public class MainActivity extends AppCompatActivity {

    private Button showButton;
    private static final String LOG_TAG = "dalbers/TimerPickerPlayground";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showButton = (Button)findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerPickerDialogFragment timerDialog = new TimerPickerDialogFragment();
                timerDialog.show(getSupportFragmentManager(),"TimePickerDialog");
                timerDialog.setDialogListener(dialogListener);
            }
        });
    }

    private TimerPickerDialogListener dialogListener = new TimerPickerDialogListener() {
        @Override
        public void timeSet(long timeInMillis) {
            Log.d(LOG_TAG,"Timer set to " + timeInMillis + "ms");
        }

        @Override
        public void dialogCanceled() {

        }
    };
}

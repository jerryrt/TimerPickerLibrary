package dalbers.com.timerpickerplayground;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import dalbers.com.timerpicker.TimerPickerDialogFragment;

public class MainActivity extends AppCompatActivity {

    private Button showButton;
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
            }
        });
    }

}

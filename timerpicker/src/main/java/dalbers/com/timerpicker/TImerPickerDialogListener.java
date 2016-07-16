package dalbers.com.timerpicker;

/**
 * Created by davidalbers on 7/16/16.
 */
public interface TimerPickerDialogListener {
    public void timeSet(long timeInMillis);
    public void dialogCanceled();
}

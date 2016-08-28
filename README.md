# TimerPickerLibrary
This library provides timer pickers for setting CountdownTimers and a TextView for displaying time left in CountdownTimers or elapsed time in a regular Timer. I'm aiming to make these views look and feel like the stock Timer app UI. This is mostly finished.

#Examples
##Create a `TimerPickerDialog`
```java
/** Create and show a timer picker */
private void showPickerDialog() {
    TimerPickerDialogFragment timerDialog = new TimerPickerDialogFragment();
    timerDialog.show(getSupportFragmentManager(), "TimerPickerDialog");
    timerDialog.setDialogListener(dialogListener);
}

/** Do stuff when timer is set in dialog or dialog is cancelled  */
private TimerPickerDialogListener dialogListener = new TimerPickerDialogListener() {
    @Override
    public void timeSet(long timeInMillis) {
        Log.d("TimerPickerDialog","Set time to " + timeInMillis + "ms");
        //Do something else
    }

    @Override
    public void dialogCanceled() {
      Log.d("TimerPickerDialog","Cancelled");
    }
};
```
Result:

<img src="/main.png" width="300">

####Change Delimiters of Dialog
You have a choice between two types of delimiters to use in the timer. The default is `hms` which is how time is displayed in the Clock app in stock Android. Here's what is looks like:

<img src="/tvHms.png" width="150">

If you need to switch back to it in code:
```java
timerDialog.setDelimiter(TimerViewUtils.DelimiterType.hms);
```

The other delimiter type is `punctuation` which uses semicolons and periods. Here's an example:

<img src="/tvPunctuation.png" width="150">

To switch to this:
```java
timerDialog.setDelimiter(TimerViewUtils.DelimiterType.punctuation);
```

##Create a `TimerTextView`
This goes inside an xml layout:
```xml
<dalbers.com.timerpicker.TimerTextView
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  android:id="@+id/timer_text_view"/>
```
####Change Delimiter and Time for `TimerTextView` 
The default delimiters are hms and and the default time is 0. To change these in XML, you need to add `xmlns:custom="http://schemas.android.com/apk/res-auto"` to the parent view of the layout. Then:
```xml
custom:time="1000" 
custom:delimiter="punctuation"
```
To programmatically change delimiters or time:
```java
TimerTextView timerTextView = (TimerTextView)findViewById(R.id.timer_text_view);
timerTextView.setDelimiterType(TimerViewUtils.DelimiterType.hms);
timerTextView.setTime(0); //0 milliseconds
```
Delimters look the same as they do in `TimerPickerDialog`.
####Additional options
To append a number to the end of the time (no effect if time is maxed out): 
```java
timerTextView.appendNumber(1);
```
To delete the last number:
```java
timerTextView.removeLastNumber();
```
To get time being displayed in milliseconds:
```java
long time = timerTextView.getTime();
```
To get time being displayed in hours, minutes, and seconds:
```java
//where hms[0] is hours, hms[1] is minutes, hms[2] is seconds
int[] hms = TimerViewUtils.millisToHMS(timerTextView.getTime());
```
#Additional Thoughts
####TimerEditText
I initially created an `EditText` that formatted typed numbers into the timer formats. Ideally, the user sets the timer by typing into the `EditText`. When the timer starts, the `EditText` can be disabled and elapsing time can be displayed in it. This is much simpler than using a `TimerPickerDialog`, however the keyboard sometimes covered the `EditText`. I tried various hacks to fix this and it seems to work in portrait mode but not landscape. `EditTextActivity.java` should offer a straightforward implementation, if you want to try it.

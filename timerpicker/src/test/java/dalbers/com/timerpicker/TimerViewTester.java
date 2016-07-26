package dalbers.com.timerpicker;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import android.content.SharedPreferences;
import android.util.Log;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by davidalbers on 7/16/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class TimerViewTester {

    @Test
    public void verifyTimerUtils() {
        //general valid string->long conversions
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("00h 00m 00s") ),         0L);
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("00h 00m 10s") ),     10000L);
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("00h 01m 00s") ),     60000L);
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("01h 00m 00s") ),   3600000L);
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("99h 99m 99s") ), 362439000L);

        //weird strings
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("")),  0L);
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("  9 9h   99m   9 9 s ")), 362439000L);
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("99 99 99")), 362439000L);
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("99d 99g 99a")), 362439000L);
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("99d 99g 9a")), 38439000L);
        assertEquals(TimerViewUtils.timeStringToMillis(
                TimerViewUtils.stringToFormattedHMS("99999911111")), 362439000L);

        //general valid long->string conversions
        assertEquals(TimerViewUtils.millisToFormattedHMS(        0L, 
                TimerViewUtils.DelimiterType.hms), "00h 00m 00s");
        assertEquals(TimerViewUtils.millisToFormattedHMS(    10000L,
                TimerViewUtils.DelimiterType.hms), "00h 00m 10s");
        assertEquals(TimerViewUtils.millisToFormattedHMS(    60000L, 
                TimerViewUtils.DelimiterType.hms), "00h 01m 00s");
        assertEquals(TimerViewUtils.millisToFormattedHMS(  3600000L, 
                TimerViewUtils.DelimiterType.hms), "01h 00m 00s");
        assertEquals(TimerViewUtils.millisToFormattedHMS(359999000L, 
                TimerViewUtils.DelimiterType.hms), "99h 59m 59s");
        assertEquals(TimerViewUtils.millisToFormattedHMS(   -60000L, 
                TimerViewUtils.DelimiterType.hms), "00h 00m 00s");

        assertEquals(TimerViewUtils.millisToFormattedHMS(        0L, 
                TimerViewUtils.DelimiterType.punctuation), "00:00.00");
        assertEquals(TimerViewUtils.millisToFormattedHMS(    10000L, 
                TimerViewUtils.DelimiterType.punctuation), "00:00.10");
        assertEquals(TimerViewUtils.millisToFormattedHMS(    60000L,
                TimerViewUtils.DelimiterType.punctuation), "00:01.00");
        assertEquals(TimerViewUtils.millisToFormattedHMS(  3600000L,
                TimerViewUtils.DelimiterType.punctuation), "01:00.00");
        assertEquals(TimerViewUtils.millisToFormattedHMS(359999000L,
                TimerViewUtils.DelimiterType.punctuation), "99:59.59");
        assertEquals(TimerViewUtils.millisToFormattedHMS(   -60000L,
                TimerViewUtils.DelimiterType.punctuation), "00:00.00");
    }

}

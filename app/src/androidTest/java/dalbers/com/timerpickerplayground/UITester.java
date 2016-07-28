package dalbers.com.timerpickerplayground;

import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;


import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.*;
/**
 * Created by davidalbers on 7/16/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITester {
    public final static String FULL_TIMER_1_TO_6_VERIFY = "12h 34m 56s";
    public final static String TIMER_7_TO_0_VERIFY = "00h 78m 90s";
    public final static String TIMER_NINE_SEC = "00h 00m 09s";
    public final static String TIMER_SEVEN_SEC = "00h 00m 07s";
    public final static String TIMER_SIX_SEC = "00h 00m 06s";
    public final static String TIMER_7_TO_0_CONVERSION = "01h 19m 30s";
    public final static String TIMER_NINE_SEC_PUNC = "00:00.09";
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void fullTimer1to6() {
        onView(withId(R.id.create_delete_timer_button)).perform(click());
        onView(withText("Create a New Timer")).check(matches(isDisplayed()));
        onView(withId(R.id.one_button)).perform(click());
        onView(withId(R.id.two_button)).perform(click());
        onView(withId(R.id.three_button)).perform(click());
        onView(withId(R.id.four_button)).perform(click());
        onView(withId(R.id.five_button)).perform(click());
        onView(withId(R.id.six_button)).perform(click());
        onView(withId(R.id.timerTextView)).check(matches(withText(FULL_TIMER_1_TO_6_VERIFY)));
        //click dialog set
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("Create a New Timer")).check(doesNotExist());
        onView(withId(R.id.timer_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.timer_text_view)).check(matches(withText(FULL_TIMER_1_TO_6_VERIFY)));
    }

    @Test
    public void timer7to0() {
        onView(withId(R.id.create_delete_timer_button)).perform(click());
        onView(withText("Create a New Timer")).check(matches(isDisplayed()));
        onView(withId(R.id.seven_button)).perform(click());
        onView(withId(R.id.eight_button)).perform(click());
        onView(withId(R.id.nine_button)).perform(click());
        onView(withId(R.id.zero_button)).perform(click());
        onView(withId(R.id.timerTextView)).check(matches(withText(TIMER_7_TO_0_VERIFY)));
        //click dialog set
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("Create a New Timer")).check(doesNotExist());
        onView(withId(R.id.timer_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.timer_text_view)).check(matches(withText(TIMER_7_TO_0_CONVERSION)));
    }

    @Test
    public void cancelDialog() {
        onView(withId(R.id.create_delete_timer_button)).perform(click());
        onView(withText("Create a New Timer")).check(matches(isDisplayed()));
        //click dialog cancel
        onView(withId(android.R.id.button2)).perform(click());
        onView(withText("Create a New Timer")).check(doesNotExist());
        onView(withId(R.id.timer_text_view)).check(matches(not(isDisplayed())));
    }

    @Test
    public void runTimer() {
        onView(withId(R.id.create_delete_timer_button)).perform(click());
        onView(withText("Create a New Timer")).check(matches(isDisplayed()));
        onView(withId(R.id.nine_button)).perform(click());
        onView(withId(R.id.timerTextView)).check(matches(withText(TIMER_NINE_SEC)));
        //click dialog set
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("Create a New Timer")).check(doesNotExist());
        onView(withId(R.id.timer_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.start_stop_button)).perform(click());
        // Wait for some time to elapse in the timer
        // this is probably a sketchy way to do it
        SystemClock.sleep(1500L);
        //stop timer, make sure it actually stops
        onView(withId(R.id.start_stop_button)).perform(click());
        onView(withId(R.id.timer_text_view)).check(matches(withText(TIMER_SEVEN_SEC)));
        SystemClock.sleep(2500L);
        onView(withId(R.id.timer_text_view)).check(matches(withText(TIMER_SEVEN_SEC)));
        //start and stop again
        onView(withId(R.id.start_stop_button)).perform(click());
        SystemClock.sleep(1500L);
        onView(withId(R.id.start_stop_button)).perform(click());
        onView(withId(R.id.timer_text_view)).check(matches(withText(TIMER_SIX_SEC)));
        //stop and run to completion
        onView(withId(R.id.start_stop_button)).perform(click());
        SystemClock.sleep(10000L);
        onView(withId(R.id.timer_text_view)).check(matches(not(isDisplayed())));
    }

    @Test
    public void killRunningTimer() {
        onView(withId(R.id.create_delete_timer_button)).perform(click());
        onView(withText("Create a New Timer")).check(matches(isDisplayed()));
        onView(withId(R.id.nine_button)).perform(click());
        onView(withId(R.id.timerTextView)).check(matches(withText(TIMER_NINE_SEC)));
        //click dialog set
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("Create a New Timer")).check(doesNotExist());
        onView(withId(R.id.timer_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.start_stop_button)).perform(click());
        // Wait for some time to elapse in the timer
        // this is probably a sketchy way to do it
        SystemClock.sleep(1500L);
        onView(withId(R.id.create_delete_timer_button)).perform(click());
        onView(withId(R.id.timer_text_view)).check(matches(not(isDisplayed())));
    }

    @Test
    public void switchDelimiters() {
        onView(withId(R.id.create_delete_timer_button)).perform(click());
        onView(withText("Create a New Timer")).check(matches(isDisplayed()));
        onView(withId(R.id.nine_button)).perform(click());
        onView(withId(R.id.timerTextView)).check(matches(withText(TIMER_NINE_SEC)));
        //click dialog set
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("Create a New Timer")).check(doesNotExist());
        onView(withId(R.id.timer_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.timer_text_view)).check(matches(withText(TIMER_NINE_SEC)));
        //click punctuation in spinner and retest
        onView(withId(R.id.delimiterSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Punctuation"))).perform(click());
        onView(withId(R.id.timer_text_view)).check(matches(withText(TIMER_NINE_SEC_PUNC)));
        onView(withId(R.id.create_delete_timer_button)).perform(click());
        onView(withId(R.id.create_delete_timer_button)).perform(click());
        onView(withText("Create a New Timer")).check(matches(isDisplayed()));
        onView(withId(R.id.nine_button)).perform(click());
        onView(withId(R.id.timerTextView)).check(matches(withText(TIMER_NINE_SEC_PUNC)));
    }
}

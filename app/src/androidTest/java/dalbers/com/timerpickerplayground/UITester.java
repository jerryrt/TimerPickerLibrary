package dalbers.com.timerpickerplayground;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;


import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
/**
 * Created by davidalbers on 7/16/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITester {
    public final static String FULL_TIMER_1_TO_6_VERIFY = "12h 34m 56s";
    public final static String TIMER_7_TO_0_VERIFY = "00h 78m 90s";
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void fullTimer1to6() {
        onView(withId(R.id.show_button)).perform(click());
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
    }

    @Test
    public void timer7to0() {
        onView(withId(R.id.show_button)).perform(click());
        onView(withText("Create a New Timer")).check(matches(isDisplayed()));
        onView(withId(R.id.seven_button)).perform(click());
        onView(withId(R.id.eight_button)).perform(click());
        onView(withId(R.id.nine_button)).perform(click());
        onView(withId(R.id.zero_button)).perform(click());
        onView(withId(R.id.timerTextView)).check(matches(withText(TIMER_7_TO_0_VERIFY)));
        //click dialog set
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("Create a New Timer")).check(doesNotExist());
    }

    @Test
    public void cancelDialog() {
        onView(withId(R.id.show_button)).perform(click());
        onView(withText("Create a New Timer")).check(matches(isDisplayed()));
        //click dialog cancel
        onView(withId(android.R.id.button2)).perform(click());
        onView(withText("Create a New Timer")).check(doesNotExist());
    }

}

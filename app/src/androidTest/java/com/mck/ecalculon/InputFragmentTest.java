package com.mck.ecalculon;

import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by mike on 5/3/2015.
 */
public class InputFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

    @Before
    public void setUp(){
        getActivity();
    }

    public InputFragmentTest(){
        super(MainActivity.class);
    }

    @Test
    public void testCanClickNumberButtons(){
        onView(allOf(withText("0"),
                withContentDescription("number button"))).perform(click());
        onView(allOf(withText("1"),
                withContentDescription("number button"))).perform(click());
        onView(withText("2")).perform(click());
        onView(withText("3")).perform(click());
        onView(withText("4")).perform(click());
        onView(withText("5")).perform(click());
        onView(withText("6")).perform(click());
        onView(withText("7")).perform(click());
        onView(withText("8")).perform(click());
        onView(withText("9")).perform(click());
    }

    @Test
    public void testCanClickEqualsButton(){
        onView(withText("=")).perform(click());
    }

    @Test
    public void testCanClickDecimalButton(){
        onView(withText(".")).perform(click());
    }

    @Test
    public void testCanClickMenuButtons(){
        onView(withText("Clear")).perform(click());
        onView(withText("Undo")).perform(click());
    }

    @Test
    public void testCanClickBasicOperatorButtons(){
        onView(withText("/")).perform(click());
        onView(withText("x")).perform(click());
        onView(withText("+")).perform(click());
        onView(withText("-")).perform(click());
    }

}
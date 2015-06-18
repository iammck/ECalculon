package com.mck.ecalculon;

import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by mike on 6/6/2015.
 */
public class ExtendedOperationsTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public ExtendedOperationsTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
        getActivity();
    }

    @Test
    public void testCanClick() {
        // can click each (, ), and pi
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withText("(")).perform(click());
        onView(withText(")")).perform(click());
        //onView(withText("&#960;")).perform(click());
        onView(withContentDescription("pi")).perform(click());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withText("(")).perform(click());
        onView(withText(")")).perform(click());
        //onView(withText("&#960;")).perform(click());
        onView(withContentDescription("pi")).perform(click());

    }



}

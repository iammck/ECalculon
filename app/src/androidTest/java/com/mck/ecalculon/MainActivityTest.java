package com.mck.ecalculon;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by mike on 5/2/2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void beforeTesting(){
        getActivity();
    }

    @Test
    public void testHasDisplayedECalculonFragment(){
        String description = getActivity().getResources()
                .getString(R.string.ecalculon_fragment_content_description);
        onView(withContentDescription(description)).check(matches(isDisplayed()));
    }
}

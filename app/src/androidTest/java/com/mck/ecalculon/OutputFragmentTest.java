package com.mck.ecalculon;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by mike on 5/2/2015.
 */
public class OutputFragmentTest  extends ActivityInstrumentationTestCase2<MainActivity> {
    public OutputFragmentTest(){
        super(MainActivity.class);
    }

    @Test
    public void testInitialOutputText(){
        getActivity();
        onView(withId(R.id.output))
                .check(matches(withText(R.string.default_output)));
    }
}

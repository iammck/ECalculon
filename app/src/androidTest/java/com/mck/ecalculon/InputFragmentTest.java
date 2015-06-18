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

    private void clickButton(String text) {
        clickButton(text, null);
    }
    private void clickButton(String text, String description){
        if (description == null){
            onView(withText(text)).perform(click());
        } else if (text == null){
            onView(withContentDescription(description)).perform(click());
        } else {
            onView(allOf(withText(text),
                    withContentDescription(description))).perform(click());
        }
    }

    private void clickButtons(String buttons){
        for( int index = 0 ; index < buttons.length(); index++){
            clickButton(String.valueOf( buttons.charAt(index)));
        }
    }

    @Test
    public void testCanClickNumberButtons(){
        clickButtons("1234567890");
    }

    @Test
    public void testCanClickEqualsButton(){
        clickButton("=");
    }

    @Test
    public void testCanClickDecimalButton(){
        clickButton(".");
    }

    @Test
    public void testCanClickMenuButtons(){
        clickButton("Clear");
        clickButton("Undo");
    }

    @Test
    public void testCanClickBasicOperatorButtons(){
        clickButtons("2/2x2+2-");
   }

    @Test
    public void testCanClickParenthesis(){
        clickButtons("(2)");
    }

    @Test
    public void testCanEatPi(){
        clickButton(null, "pi");
    }
}
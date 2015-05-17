package com.mck.ecalculon;

import android.test.ActivityInstrumentationTestCase2;

import com.mck.ecalculon.evaluator.Evaluator;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * When working with numbers v full numbers. a number is a single numbe 0-9,
 * while a full number represents something like 45, 273, 2.4 or 0.8765309.
 * A full Integer is like the first two, 45 and 273, but not like the other two.
 *
 * Created by mike on 5/2/2015.
 */
public class ECalculonFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private ECalculonFragment eCalculonFragment;

    public ECalculonFragmentTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
        eCalculonFragment = (ECalculonFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.ecalculon_fragment_container);
    }

    ////////////
    // SET UP //
    ////////////

    @Test
    public void testHasDisplayedOutputFragment() {
        String description = getActivity()
                .getResources()
                .getString(R.string.output_fragment_content_description);
        onView(withContentDescription(description))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testHasDisplayedInputFragment() {
        String description = getActivity()
                .getResources()
                .getString(R.string.input_fragment_content_description);
        onView(withContentDescription(description))
                .check(matches(isDisplayed()));
    }

    /////////////
    // NUMBERS //
    /////////////

    @Test
    public void testHandleNumberInputAsInitialOutput() {
        // Clicking on any number, say zero, should put it onto the screen.
        onView(withId(R.id.buttonNumber0)).perform(click());
        // note here that if the output was "0" already, it should be the same.
        onView(withId(R.id.output)).check(matches(withText("0")));
        // it should also be removed from the numbers, leaving just "0"
        assertEquals("The first item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(0));

    }

    public void testHandleNumberInputAsInitailOutputWithError() {
        setOutputToERROR();

        // clicking on all numbers should insert them on the right.
        onView(withId(R.id.buttonNumber0)).perform(click());
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonNumber2)).perform(click());
        onView(withId(R.id.buttonNumber3)).perform(click());
        onView(withId(R.id.buttonNumber4)).perform(click());
        onView(withId(R.id.buttonNumber5)).perform(click());
        onView(withId(R.id.buttonNumber6)).perform(click());
        onView(withId(R.id.buttonNumber7)).perform(click());
        onView(withId(R.id.buttonNumber8)).perform(click());
        onView(withId(R.id.buttonNumber9)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("0123456789")));
        // assert mainActivity inputTypeHistory is the right size.
        assertEquals("eCalculonFragment inputTypeHistory is not 10 as expected",
                10, eCalculonFragment.getInputTypeHistory().size());
        // main activity numbers should be a list with just the one number
        assertEquals("eCalculonFragment should have a list with just the one number",
                1, eCalculonFragment.getNumbers().size());

    }


    //////////////
    // OPERATOR //
    //////////////

    @Test
    public void testHandleOperatorInputAsInitialOutput(){
        // should not be able to start expression with /,x, or +, but can with -
        onView(withId(R.id.buttonDivision)).perform(click());
        onView(withId(R.id.buttonMultiplication)).perform(click());
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.buttonSubtraction)).perform(click());
        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("-")));
        // assert fragment lists are correct
        assertEquals("Input type history size should be one",
                1, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be one",
                1, eCalculonFragment.getNumbers().size());
        // This insert should remove the leading zero, error sign.
        assertEquals("The operator should be a negative sign",
                "-", eCalculonFragment.getOperators().get(0));

        // should not be able to continue expression with /,x, or +, but can with -
        onView(withId(R.id.buttonDivision)).perform(click());
        onView(withId(R.id.buttonMultiplication)).perform(click());
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.buttonSubtraction)).perform(click());

        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("--")));
        // assert fragment lists are correct
        assertEquals("Input type history size should be two",
                2, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be one",
                1, eCalculonFragment.getNumbers().size());
        assertEquals("There should be two operators",
                2, eCalculonFragment.getOperators().size());
        assertEquals("The second operator should be a negative sign",
                "-", eCalculonFragment.getOperators().get(1));
    }

    @Test
    public void testHandleOperatorInputAsInitialOutputWithError() {
        setOutputToERROR();
        testHandleOperatorInputAsInitialOutput();

    }

    @Test
    public void testHandleOperatorInputWithBinarayOperatorAfterInitialOutput(){
        // add an initial number and operator
        onView(withId(R.id.buttonNumber9)).perform(click());
        onView(withId(R.id.buttonDivision)).perform(click());

        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("9/")));
        // assert activity lists are correct
        assertEquals("Input type history size should be 2",
                2, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 2",
                2, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 1",
                1, eCalculonFragment.getOperators().size());
        assertEquals("The only operator should be a division sign",
                "/", eCalculonFragment.getOperators().get(0));
        assertEquals("The second item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(1));

        // multiplication
        onView(withId(R.id.buttonNumber9)).perform(click());
        onView(withId(R.id.buttonMultiplication)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("9/9x")));
        // addition
        onView(withId(R.id.buttonNumber9)).perform(click());
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("9/9x9+")));
        // addition
        onView(withId(R.id.buttonNumber4)).perform(click());
        onView(withId(R.id.buttonSubtraction)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("9/9x9+4-")));

        // assert activity lists are correct
        assertEquals("Input type history size should be 8",
                8, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 5",
                5, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 4",
                4, eCalculonFragment.getOperators().size());
        assertEquals("The last operator should be a subtraction sign",
                "-", eCalculonFragment.getOperators().get(3));
        assertEquals("The last item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(4));
    }


    @Test
    public void testHandleOperatorInputWithUnaryOperatorAfterInitialOutput() {
        // add an initail number and operator
        onView(withId(R.id.buttonNumber9)).perform(click());
        onView(withId(R.id.buttonDivision)).perform(click());
        onView(withId(R.id.buttonSubtraction)).perform(click());

        // assert correct.
        onView(withId(R.id.output)).check(matches(withText("9/-")));
        // assert activity lists are correct
        assertEquals("Input type history size should be 3",
                3, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 2",
                2, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 2",
                2, eCalculonFragment.getOperators().size());
        assertEquals("The last operator should be a subtraction sign",
                "-", eCalculonFragment.getOperators().get(1));
        assertEquals("The last item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(1));
    }

    /////////////
    // DECIMAL //
    /////////////

    @Test
    public void testHandleDecimalInputForInitialOutput(){
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("0.")));
        // Since there is already a decimal do not add another.
        onView(withId(R.id.buttonDecimal)).perform(click());
        // assert the output is correct.
        onView(withId(R.id.output)).check(matches(withText("0.")));
        // assert input type history has size of one
        assertEquals("Input type history size should be one",
                1, eCalculonFragment.getInputTypeHistory().size());
    }

    @Test
    public void testHandleDecimalInputForErrorInitialOutput(){
        setOutputToERROR();
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("0.")));
        // Since there is already a decimal do not add another.
        onView(withId(R.id.buttonDecimal)).perform(click());
        // assert the output is correct.
        onView(withId(R.id.output)).check(matches(withText("0.")));
        // assert input type history has size of one
        assertEquals("Input type history size should be one",
                1, eCalculonFragment.getInputTypeHistory().size());
    }

    @Test
    public void testHandleDecimalInputForMultipleNumbers(){
        // first number
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("0.")));
        onView(withId(R.id.buttonNumber6)).perform(click());

        // operator
        onView(withId(R.id.buttonAddition)).perform(click());

        // second number
        onView(withId(R.id.buttonNumber9)).perform(click());
        // try to insert two decimals
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonNumber5)).perform(click());
        // assert the right output
        onView(withId(R.id.output)).check(matches(withText("0.6+9.5")));

        // operator
        onView(withId(R.id.buttonAddition)).perform(click());

    }

    @Test
    public void testHandleDecimalInputAfterOperatorInput(){
        onView(withId(R.id.buttonNumber3)).perform(click());
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.buttonDecimal)).perform(click());
        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("3+0.")));
        // assert activity lists are correct
        assertEquals("Input type history size should be 3",
                3, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 2",
                2, eCalculonFragment.getNumbers().size());
        assertEquals("There should be 1 operator",
                1, eCalculonFragment.getOperators().size());
    }

    ////////////
    // EQUALS //
    ////////////

    private class MockEvaluator extends Evaluator {
        public boolean hasBeenEvaluated = false;

        @Override
        public String evaluate(String expression){
            this.hasBeenEvaluated = true;
            return "0";
        }
    }

    @Test
    public void testInputEquals(){
        MockEvaluator mockEvaluator = new MockEvaluator();
        // equals input should send the output text to an evaluator
        eCalculonFragment.setEvaluator(mockEvaluator);
        onView(withId(R.id.buttonEquals)).perform(click());
        assertTrue("MockEvaluator should have been evaluated.",
                mockEvaluator.hasBeenEvaluated);
    }


    @Test
    public void testEqualsForEquations() {
        onView(withId(R.id.buttonNumber6)).perform(click());
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.buttonNumber3)).perform(click());
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonNumber2)).perform(click());
        onView(withId(R.id.buttonEquals)).perform(click());
        String output = "9.2";
        onView(withId(R.id.output)).check(matches(withText("9.2")));
        // if we enter in an operator, then want to use the number.
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.buttonNumber3)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("9.2+3")));
        onView(withId(R.id.buttonEquals)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("12.2")));
        // if enter in a number, then remove number
        onView(withId(R.id.buttonNumber3)).perform(click());
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.buttonNumber3)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("3+3")));
        onView(withId(R.id.buttonEquals)).perform(click());
        onView(withId(R.id.output)).check(matches(withText("6")));

    }

    @Test
    public void testEqualsForErrors() {
        onView(withId(R.id.buttonNumber6)).perform(click());
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.buttonNumber3)).perform(click());
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonNumber2)).perform(click());
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.buttonEquals)).perform(click());
        String output = "ERROR";
        onView(withId(R.id.output)).check(matches(withText(output)));

        onView(withId(R.id.buttonNumber6)).perform(click());
        onView(withId(R.id.buttonDivision)).perform(click());
        onView(withId(R.id.buttonNumber0)).perform(click());
        onView(withId(R.id.buttonEquals)).perform(click());
        output = "ERROR";
        onView(withId(R.id.output)).check(matches(withText(output)));


    }



    ///////////
    // CLEAR //
    ///////////

    @Test
    public void testHandleClearInput(){
        getActivity();
        // The output starts with this value
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonAddition)).perform(click());
        onView(withId(R.id.buttonNumber3)).perform(click());
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonNumber3)).perform(click());

        // click the clear button
        onView(withId(R.id.buttonClear)).perform(click());
        // check
        onView(withId(R.id.output)).check(matches(withText("0")));

        // assert activity has one numbers item, a "0",
        assertEquals("The expected numbers size should be 1",
                1, eCalculonFragment.getNumbers().size());
        assertEquals("Clear all should leave a \"0\"",
                "0", eCalculonFragment.getNumbers().get(0));
        // assert the there are no operators.
        assertEquals("The expected operators size should be 0",
                0, eCalculonFragment.getOperators().size());
        // assert mainActivity inputTypeHistory is the right size.
        assertEquals("The epected input history size is not 0",
                0, eCalculonFragment.getInputTypeHistory().size());
    }

    //////////
    // UNDO //
    //////////

    @Test
    public void testHandleUndoInputForNumbers(){
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonUndo)).perform(click());

        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("1")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 1",
                1, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 1",
                1, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 0",
                0, eCalculonFragment.getOperators().size());
        assertEquals("The item in numbers list should be \"1\" ",
                "1", eCalculonFragment.getNumbers().get(0));


        // insert /11.3 and remove .3
        onView(withId(R.id.buttonDivision)).perform(click());
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonNumber3)).perform(click());
        onView(withId(R.id.buttonUndo)).perform(click());
        onView(withId(R.id.buttonUndo)).perform(click());
        // the number is now 1/11
        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("1/11")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 4",
                4, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 2",
                2, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 1",
                1, eCalculonFragment.getOperators().size());
        assertEquals("The second item in numbers list should be \"11\" ",
                "11", eCalculonFragment.getNumbers().get(1));

    }


    @Test
    public void testHandleUndoInputForBinaryOperator(){
        // should be able to add a number
        onView(withId(R.id.buttonNumber9)).perform(click());
        // should be able to add any operator
        onView(withId(R.id.buttonDivision)).perform(click());
        // should be able to remove an operator
        onView(withId(R.id.buttonUndo)).perform(click());

        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("9")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 1",
                1, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 1",
                1, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 0",
                0, eCalculonFragment.getOperators().size());

    }

    @Test
    public void testHandleUndoInputForUnaryOperatorForInitialInput(){
        onView(withId(R.id.buttonSubtraction)).perform(click());
        onView(withId(R.id.buttonUndo)).perform(click());

        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("0")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 0",
                0, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 1",
                1, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 0",
                0, eCalculonFragment.getOperators().size());
        assertEquals("The item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(0));
    }


    @Test
    public void testHandleUndoInputForUnaryOperatorForMultipleNumbers() {
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonDivision)).perform(click());
        onView(withId(R.id.buttonSubtraction)).perform(click());
        onView(withId(R.id.buttonUndo)).perform(click());

        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("11/")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 3",
                3, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 2",
                2, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 1",
                1, eCalculonFragment.getOperators().size());
        assertEquals("The second item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(1));

    }

    @Test
    public void testHandleUndoInputForDecimal(){
        // a leading decimal
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonUndo)).perform(click());
        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("0")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 0",
                0, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 1",
                1, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 0",
                0, eCalculonFragment.getOperators().size());
        assertEquals("The item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(0));

        // a leading decimal after first operator
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonDivision)).perform(click());
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonUndo)).perform(click());
        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("1/")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 2",
                2, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 2",
                2, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 1",
                1, eCalculonFragment.getOperators().size());
        assertEquals("The second item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(1));

    }

    @Test
    public void testHandleUndoInputForDecimalWithUserInsertedZero(){
        // a leading decimal
        onView(withId(R.id.buttonNumber0)).perform(click());
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonUndo)).perform(click());
        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("0")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 0, last item is ",
                0, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 1",
                1, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 0",
                0, eCalculonFragment.getOperators().size());
        assertEquals("The first item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(0));

        // a leading decimal after first operator
        onView(withId(R.id.buttonNumber1)).perform(click());
        onView(withId(R.id.buttonDivision)).perform(click());
        onView(withId(R.id.buttonNumber0)).perform(click());
        onView(withId(R.id.buttonDecimal)).perform(click());
        onView(withId(R.id.buttonUndo)).perform(click());
        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("1/")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 2",
                2, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 2",
                2, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 1",
                1, eCalculonFragment.getOperators().size());
        assertEquals("The second item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(1));

    }

    @Test
    public void testHandleUndoAsInitialInput(){
        onView(withId(R.id.buttonUndo)).perform(click());
        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("0")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 0",
                0, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 1",
                1, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 0",
                0, eCalculonFragment.getOperators().size());
        assertEquals("The first item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(0));
    }
    @Test
    public void testHandleUndoAsInputAfterERROR(){
        setOutputToERROR();
        onView(withId(R.id.buttonUndo)).perform(click());
        // Assert correct output
        onView(withId(R.id.output)).check(matches(withText("0")));
        // assert eCalculonFragment lists are correct
        assertEquals("Input type history size should be 0",
                0, eCalculonFragment.getInputTypeHistory().size());
        assertEquals("The expected numbers size should be 1",
                1, eCalculonFragment.getNumbers().size());
        assertEquals("The expected operators size should be 0",
                0, eCalculonFragment.getOperators().size());
        assertEquals("The first item in numbers list should be \"0\" ",
                "0", eCalculonFragment.getNumbers().get(0));
    }

    private void setOutputToERROR(){
        eCalculonFragment.setWasError(true);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OutputFragment outFrag = (OutputFragment) eCalculonFragment
                        .getChildFragmentManager()
                        .findFragmentById(R.id.output_fragment_container);
                outFrag.setError();
            }
        });
        // give the screen a sec to update.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
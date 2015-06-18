package com.mck.ecalculon;

import android.test.ActivityInstrumentationTestCase2;

import com.mck.ecalculon.evaluator.Evaluator;

import org.junit.Before;

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

    // TODO after equals new numbers should remove answer first.

    private ECalculonFragment eCalculonFragment;

    public ECalculonFragmentTest() {
        super(MainActivity.class);
    }

    private void clickButton(String text) {
        onView(withText(text)).perform(click());
    }
    private void clickButtonWithDesc(String text) {
        onView(withContentDescription(text)).perform(click());
    }

    private void clickButtons(String buttons){
        for(int index = 0; index < buttons.length(); index++){
            String button = String.valueOf(buttons.charAt(index));
            clickButton(button);
        }
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


    public void testHasDisplayedOutputFragment() {
        String description = getActivity()
                .getResources()
                .getString(R.string.output_fragment_content_description);
        onView(withContentDescription(description))
                .check(matches(isDisplayed()));
    }


    public void testHasDisplayedInputFragment() {
        String description = getActivity()
                .getResources()
                .getString(R.string.input_fragment_content_description);
        onView(withContentDescription(description))
                .check(matches(isDisplayed()));
    }

    public void checkOutputMatches(String expected){
        onView(withId(R.id.output)).check(matches(withText(expected)));

    }

    /////////////
    // NUMBERS //
    /////////////


    public void testHandleNumberInputAsInitialOutput() {
        // Clicking on any number, say "1", should put it onto the screen.
        clickButton("1");
        checkOutputMatches("1");
    }


    public void testHandleNumberInputAsInitilOutputWithError() {
        setOutputToERROR();
        checkOutputMatches("ERROR");
        clickButton("3");
        checkOutputMatches("3");
    }


    public void testNumbersAreNotSpaced(){
        clickButtons("34");
        checkOutputMatches("34");
    }


    public void testNumbersInsertToRight(){
        clickButtons("345");
        checkOutputMatches("345");
    }


    //////////////
    // OPERATOR //
    //////////////



    public void testHandleOperatorInputAsInitialOutput() {
        // should not be able to start expression with /,x, or +, but can with -
        clickButtons("/x+-");
        checkOutputMatches("-");

    }

    public void testOperatorsAfterNegationInitailOutput(){
        // should not be able to use these operators after initial negation.
        clickButtons("-/x+");
        clickButtonWithDesc("minus");
        checkOutputMatches("-");
    }

    public void testOperatorInputWithErrorInitialOutput() {
        setOutputToERROR();
        clickButton("/");
    }

    public void testDivisionAfterNumber(){
        clickButtons("9/");
        checkOutputMatches("9 /");
    }

    public void testMultiplicationAfterNumber(){
        clickButtons("9x");
        checkOutputMatches("9 x");
    }

    public void testAddfterNumber(){
        clickButtons("9+");
        checkOutputMatches("9 +");
    }

    public void testSubtractAfterNumber(){
        clickButtons("9-");
        checkOutputMatches("9 -");
    }

    public void testNegateAfterSubtract(){
        clickButtons("9--");
        checkOutputMatches("9 - -");
    }


    public void testNegateOnlyOnceAfterSubtract(){
        clickButtons("9---");
        checkOutputMatches("9 - -");
    }

    public void testNegateOnlyOnceAfterAdd(){
        clickButtons("9+-");
        checkOutputMatches("9 + -");
    }

    /////////////
    // DECIMAL //
    /////////////


    public void testDecimalInitialOutput(){
        clickButton(".");
        checkOutputMatches("0.");
    }


    public void testNumberAfterDecimalInitialOutput(){
        clickButtons(".+");
        checkOutputMatches("0.0 +");
    }

    public void testDecimalWithERRORInitialOutput(){
        setOutputToERROR();
        clickButton(".");
        checkOutputMatches("0.");
    }

    public void testNumbersCanHaveDecimal(){
        clickButtons("1.1+.1");
        checkOutputMatches("1.1 + 0.1");
    }

    public void testNumberCanOnlyHaveOneDecimal(){
        clickButtons("1..1.");
        checkOutputMatches("1.1");
    }

    public void testDecimalInputAfterOperatorInput(){
        clickButtons("3+.");
        checkOutputMatches("3 + 0.");
    }

    ////////////
    // EQUALS //
    ////////////

    public void testEqualsForOutput() {
        clickButtons("1+1=");
        checkOutputMatches("2");
    }

    public void testNumberAfterEquals() {
        clickButtons("1+1=");
        checkOutputMatches("2");
        clickButtons("1");
        checkOutputMatches("1");
    }

    public void testOperatorAfterEquals() {
        clickButtons("1+1=");
        checkOutputMatches("2");
        clickButtons("-");
        checkOutputMatches("2 -");
    }

    public void testLeftParenthesisAfterEquals() {
        clickButtons("1+1=");
        checkOutputMatches("2");
        clickButtons("(");
        checkOutputMatches("(");
    }

    public void testRightParenthesisAfterEquals() {
        clickButtons("1+1=");
        checkOutputMatches("2");
        clickButtons(")");
        checkOutputMatches("0");
    }

    public void testEqualsForErrorReturn() {
        clickButtons("1+=");
        checkOutputMatches("ERROR");
    }

    ///////////
    // CLEAR //
    ///////////

    public void testClear(){
        clickButton("Clear");
        checkOutputMatches("0");
    }

    public void testClearNumber(){
        clickButtons("98");
        clickButton("Clear");
        checkOutputMatches("0");
    }

    public void testClearNumbers(){
        clickButtons("98+8");
        clickButton("Clear");
        checkOutputMatches("0");
    }

    //////////
    // UNDO //
    //////////

    public void testUndo(){
        clickButton("Undo");
        checkOutputMatches("0");
    }

    public void testUndoNumber(){
        clickButton("1");
        clickButton("Undo");
        checkOutputMatches("0");
    }

    public void testUndoBigNumber(){
        clickButtons("12");
        clickButton("Undo");
        checkOutputMatches("1");
    }

    public void testUndoSubtract(){
        clickButtons("1-");
        clickButton("Undo");
        checkOutputMatches("1");
    }

    public void testUndoNegation(){
        clickButton("-");
        clickButton("Undo");
        checkOutputMatches("0");
    }

    public void testUndoNegationAfterSubtract(){
        clickButtons("1--");
        clickButton("Undo");
        checkOutputMatches("1 -");
    }

    public void testUndoDecimal(){
        clickButtons("1.");
        clickButton("Undo");
        checkOutputMatches("1");
    }

    public void testHandleUndoAsInputAfterERROR(){
        setOutputToERROR();
        clickButton("Undo");
        checkOutputMatches("0");
    }

    private void setOutputToERROR(){
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
            Thread.sleep(850);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /////////////////
    // PARENTHESIS //
    /////////////////

    public void testButtonsProduceCorrectOutput() {
        clickButton("(");
        clickButtonWithDesc("pi");
        clickButton(")");
        checkOutputMatches("( 3.14 )");
    }

    public void testLeftParenthesisButtons() {
        insertCheckClear("-(", "- (");
        insertCheckClear("-((", "- ( (");
        insertCheckClear("(-", "( -");
        insertCheckClear("(+", "(");
        insertCheckClear("(3", "( 3");
        insertCheckClear("(3(", "( 3");
        insertCheckClear("3+(", "3 + (");
        insertCheckClear("3+(-", "3 + ( -");
        insertCheckClear("3+(+", "3 + (");
        insertCheckClear("3+2(", "3 + 2");
        insertCheckClear("3+(2", "3 + ( 2");
        insertCheckClear("3+-(2", "3 + - ( 2");
        insertCheckClear("3+-(-2", "3 + - ( - 2");

        insertCheckClear("(.3", "( 0.3");
        insertCheckClear("(.3(", "( 0.3");
        insertCheckClear("3+2(", "3 + 2");
        insertCheckClear("3+(.", "3 + ( 0.");
        insertCheckClear("3+-(-.", "3 + - ( - 0.");
    }

    public void testRightParenthesisButtons() {
        insertCheckClear(")9", "9");
        insertCheckClear("(3))", "( 3 )");
        insertCheckClear("1+((3))", "1 + ( ( 3 ) )");
        insertCheckClear("(3+)", "( 3 +");
        insertCheckClear("(3-)", "( 3 -");
        insertCheckClear("(3)3", "( 3 )");
        insertCheckClear("(3)+", "( 3 ) +");
        insertCheckClear("1+((3)+)", "1 + ( ( 3 ) +");
        insertCheckClear("1+((3)+()", "1 + ( ( 3 ) + (");
        insertCheckClear("1+((3)+(2))", "1 + ( ( 3 ) + ( 2 ) )");
        insertCheckClear("1+((3)(", "1 + ( ( 3 )");
        insertCheckClear("1+((3)+(4+5)-2)", "1 + ( ( 3 ) + ( 4 + 5 ) - 2 )");

        insertCheckClear(").", "0.");
        insertCheckClear("(.)", "( 0.0 )");
        insertCheckClear("(3.).", "( 3.0 )");
    }

    public void testUndoWithParenthesis(){
        // U = Undo
        insertCheckClear("(U", "0");
        insertCheckClear("8+(-UUUU", "0");
        insertCheckClear("8+(--UUUUU", "0");
        insertCheckClear("(8+8)-UU8", "( 8 + 88");
        insertCheckClear("8-(-UUUU", "0");
        insertCheckClear("8--(-UUUUU", "0");
        insertCheckClear("8+(8-4)-UU", "8 + ( 8 - 4");

    }

    private void insertCheckClear(String insertString, String expectedResult) {
        for(int index = 0; index < insertString.length(); index++){
            String buttonText = String.valueOf(insertString.charAt(index));
            buttonText = (buttonText.equals("U"))? "Undo" : buttonText;
            onView(withText(buttonText)).perform(click());
        }
        onView(withId(R.id.output)).check(matches(withText(expectedResult)));
        onView(withText("Clear")).perform(click());
    }

}
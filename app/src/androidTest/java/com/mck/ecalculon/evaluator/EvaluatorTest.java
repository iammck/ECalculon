package com.mck.ecalculon.evaluator;

import junit.framework.TestCase;

/**
 * Created by mike on 5/11/2015.
 */
public class EvaluatorTest extends TestCase {

    public void testBinaryOperations() throws EvaluationException {
        assertEvaluate("52","43 + 9");
        Evaluator evaluator = new Evaluator();

        assertEvaluate("52", "43 + 9");
        assertEvaluate("21", "3 + 3 + 3 + 3 + 3 + 3 + 3");

        assertEvaluate("34", "43 - 9");
        assertEvaluate("0", "21 - 3 - 3 - 3 - 3 - 3 - 3 - 3");

        assertEvaluate("5", "45 / 9");
        assertEvaluate("1", "1000 / 10 / 10 / 10");

        assertEvaluate("45","5 x 9");
        assertEvaluate("32","2 x 2 x 2 x 2 x 2");

        assertEvaluate("7", "1 + 2 x 3");
        assertEvaluate("5", "1 + 2 x 6 / 3");
        assertEvaluate("6", "1 + 2 x 6 / 3 + 1");

    }

    public void testEvaluateWithParentheses() throws EvaluationException {
        assertEvaluate("3", "( 3 )");
        assertEvaluate("-3", "- ( 3 )");
        assertEvaluate("4", "( 3 + 1 )");
        assertEvaluate("-4", "- ( 3 + 1 )");
        assertEvaluate("6", "( 3 + 1 ) + ( 2 )");
        assertEvaluate("6", "( ( 3 + 1 ) + ( 2 ) )");
        assertEvaluate("-6", "( - ( 3 + 1 ) + - ( 2 ) )");
        assertEvaluate("4", "( ( 3 + 1 ) + ( 2 ) ) - 2");
    }

    private void assertEvaluate(String expected, String input) throws EvaluationException {
        Evaluator evaluator = new Evaluator();
        assertEquals("The input " + input + " should evaluated to "
                + expected + ".", expected, evaluator.evaluate(input));
    }

    public void testUnaryOperations() throws EvaluationException {
        Evaluator evaluator = new Evaluator();

        assertEquals("Should evaluate to -1", "-1", evaluator.evaluate("- 1"));
        assertEquals("Should evaluate to 1", "1", evaluator.evaluate("0 - - 1"));

        assertEquals("Should evaluate to 9", "9", evaluator.evaluate("- 1 + 10"));
        assertEquals("Should evaluate to 12", "12", evaluator.evaluate("- 1 + 11 - - 2"));


        assertEquals("Should evaluate to -9", "-9", evaluator.evaluate("1 + - 10"));
        assertEquals("Should evaluate to -11", "-11", evaluator.evaluate("- 1 + -10"));
        assertEquals("Should evaluate to -13", "-13", evaluator.evaluate("- 1 + - 11 - 1"));

        assertEquals("Should evaluate to 3", "3", evaluator.evaluate("- 1 + 2 x 6 / 3"));
        assertEquals("Should evaluate to -2", "-2", evaluator.evaluate("1 + 2 x 6 / - 3 + 1"));
        assertEquals("Should evaluate to 4", "4", evaluator.evaluate("1 + 2 x 6 / 3 - 1"));
        assertEquals("Should evaluate to -4", "-4", evaluator.evaluate("- 1 + - 2 x - 6 / - 3 - - 1"));

    }

    public void testDecimalAsNumber() throws EvaluationException {
        Evaluator evaluator = new Evaluator();

        assertEquals("Should evaluate to 8", "8", evaluator.evaluate("4. + 4"));
        assertEquals("Should evaluate to 8.0", "8.0", evaluator.evaluate("4.0 + 4"));
        assertEquals("Should evaluate to 8.2", "8.2", evaluator.evaluate("4.1 + 4.1"));
        assertEquals("Should evaluate to 8.22", "8.22", evaluator.evaluate("4.12 + 4.10"));
        assertEquals("Should evaluate to 8.23", "8.23", evaluator.evaluate("4.1 + 4.13"));
        assertEquals("Should evaluate to 8.1", "8.1", evaluator.evaluate("4.1 + 4"));
        assertEquals("Should evaluate to 8.0001", "8.0001", evaluator.evaluate("4.0001 + 4"));

        assertEquals("Should evaluate to 0.0", "0.0", evaluator.evaluate("4.1 - 4.1"));
        assertEquals("Should evaluate to 0.02", "0.02", evaluator.evaluate("4.12 - 4.1"));
        assertEquals("Should evaluate to -0.03", "-0.03", evaluator.evaluate("4.1 - 4.13"));
        assertEquals("Should evaluate to 0.1", "0.1", evaluator.evaluate("4.1 - 4"));

        assertEquals("Should evaluate to 8.0" ,  "8.0", evaluator.evaluate("12 / 1.5"));
        assertEquals("Should evaluate to 9.0" ,  "9.0", evaluator.evaluate("13.5 / 1.5"));

        assertEquals("Should evaluate to 4.0", "4.0", evaluator.evaluate("- 0.0 + 2 x 6 / 3"));
        assertEquals("Should evaluate to 4.0", "4.0", evaluator.evaluate("- 0.0 + 2 x 6 / 3"));
        assertEquals("Should evaluate to 4.0", "4.0", evaluator.evaluate("1.5 x 8 / 3"));
        assertEquals("Should evaluate to 5.0", "5.0", evaluator.evaluate("1 + 1.5 x 8 / 3"));
        assertEquals("Should evaluate to 6.1", "6.1", evaluator.evaluate("1.1 + 2 x 6 / 3 + 1"));
        assertEquals("Should evaluate to 2.0", "2.0", evaluator.evaluate("1 + 0.0 x 6 / 3 + 1"));
    }

    public void testDecimalCreatedInAnswer() throws EvaluationException {
        // ie 2/3 0.66  or 2/5 = .4
        assertEvaluate("0.67", "2 / 3");
        assertEvaluate("0.4", "2 / 5");
    }

    public void testZeroAsInputExpression() throws EvaluationException {
        Evaluator evaluator = new Evaluator();
        assertEquals("Should return 0 for 0 as input", "0", evaluator.evaluate("0"));
        assertEquals("Should return 0 for 0 as input", "0", evaluator.evaluate("0"));
        assertEquals("Should return 0.0 for 0.0 as input", "0.0", evaluator.evaluate("0.0"));
    }

    public void testDivideByZero(){
        assertThrowsEvaluationException("1 / 0");
        assertThrowsEvaluationException("1 / .0");
        assertThrowsEvaluationException("1 / 0.0");
        assertThrowsEvaluationException("1 + 3 / 0");
        assertThrowsEvaluationException("1 + 3 / .0");
        assertThrowsEvaluationException("1 + 3 / 0.0");
    }

    // some edge cases.
    public void testMalformedExpressions(){
        assertThrowsEvaluationException(null);
        assertThrowsEvaluationException("");
        assertThrowsEvaluationException("1 +");
        assertThrowsEvaluationException("1 -");
        assertThrowsEvaluationException("1 + + 2");
        assertThrowsEvaluationException("1 / / 2");
        assertThrowsEvaluationException("1 + 2 * *");
        assertThrowsEvaluationException("1 + 2 * * 4");
        assertThrowsEvaluationException("1 + 2 x x");
        assertThrowsEvaluationException("1 + 2 x x 4");
        assertThrowsEvaluationException("- + 1");
        assertThrowsEvaluationException("- + 1 + 2");
        assertThrowsEvaluationException("1 / /");
        assertThrowsEvaluationException("1 / 2 / /");
        assertThrowsEvaluationException("1 / 4 / / 4");
    }

    protected void assertThrowsEvaluationException(String exp){
        Evaluator evaluator = new Evaluator();
        try {
            evaluator.evaluate(exp);
            fail("The expression " + exp + " should throw EvaluationException.");
        } catch (EvaluationException e) {
        }
    }
}

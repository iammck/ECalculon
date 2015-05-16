package com.mck.ecalculon.com.mck.ecalculon.evaluator;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by mike on 5/11/2015.
 */
public class EvaluatorTest extends TestCase {

    public void testBinaryOperations() throws EvaluationException {
        Evaluator evaluator = new Evaluator();

        assertEquals("Should evaluate to 52", "52", evaluator.evaluate("43+9"));
        assertEquals("Should evaluate to 21", "21", evaluator.evaluate("3+3+3+3+3+3+3"));

        assertEquals("Should evaluate to 34", "34", evaluator.evaluate("43-9"));
        assertEquals("Should evaluate to 0", "0", evaluator.evaluate("21-3-3-3-3-3-3-3"));

        assertEquals("Should evaluate to 5" ,  "5", evaluator.evaluate("45/9"));
        assertEquals("Should evaluate to 1", "1", evaluator.evaluate("1000/10/10/10"));

        assertEquals("Should evaluate to 45", "45", evaluator.evaluate("5x9"));
        assertEquals("Should evaluate to 32", "32", evaluator.evaluate("2x2x2x2x2"));

        assertEquals("Should evaluate to 7", "7", evaluator.evaluate("1+2x3"));
        assertEquals("Should evaluate to 5", "5", evaluator.evaluate("1+2x6/3"));
        assertEquals("Should evaluate to 6", "6", evaluator.evaluate("1+2x6/3+1"));

    }

    public void testUnaryOperations() throws EvaluationException {
        Evaluator evaluator = new Evaluator();

        assertEquals("Should evaluate to -1", "-1", evaluator.evaluate("-1"));
        assertEquals("Should evaluate to 1", "1", evaluator.evaluate("--1"));
        assertEquals("Should evaluate to 1", "1", evaluator.evaluate("------1"));

        assertEquals("Should evaluate to 9", "9", evaluator.evaluate("-1+10"));
        assertEquals("Should evaluate to 11", "11", evaluator.evaluate("--1+10"));
        assertEquals("Should evaluate to 12", "12", evaluator.evaluate("------1+11"));


        assertEquals("Should evaluate to 9", "9", evaluator.evaluate("-1+--10"));
        assertEquals("Should evaluate to 11", "11", evaluator.evaluate("----1+--10"));
        assertEquals("Should evaluate to 12", "12", evaluator.evaluate("------1+--11"));

        assertEquals("Should evaluate to 3", "3", evaluator.evaluate("-1+2x6/3"));
        assertEquals("Should evaluate to -2", "-2", evaluator.evaluate("1+2x6/-3+1"));
        assertEquals("Should evaluate to 4", "4", evaluator.evaluate("1+2x6/3-1"));
        assertEquals("Should evaluate to -4", "-4", evaluator.evaluate("-1+-2x-6/-3--1"));

    }

    public void testDecimalAsNumber() throws EvaluationException {
        Evaluator evaluator = new Evaluator();

        assertEquals("Should evaluate to 8", "8", evaluator.evaluate("4.+4"));
        assertEquals("Should evaluate to 8.0", "8.0", evaluator.evaluate("4.0+4"));
        assertEquals("Should evaluate to 8.2", "8.2", evaluator.evaluate("4.1+4.1"));
        assertEquals("Should evaluate to 8.22", "8.22", evaluator.evaluate("4.12+4.10"));
        assertEquals("Should evaluate to 8.23", "8.23", evaluator.evaluate("4.1+4.13"));
        assertEquals("Should evaluate to 8.1", "8.1", evaluator.evaluate("4.1+4"));
        assertEquals("Should evaluate to 8.0001", "8.0001", evaluator.evaluate("4.0001+4"));

        assertEquals("Should evaluate to 0.0", "0.0", evaluator.evaluate("4.1-4.1"));
        assertEquals("Should evaluate to 0.02", "0.02", evaluator.evaluate("4.12-4.1"));
        assertEquals("Should evaluate to -0.03", "-0.03", evaluator.evaluate("4.1-4.13"));
        assertEquals("Should evaluate to 0.1", "0.1", evaluator.evaluate("4.1-4"));

        assertEquals("Should evaluate to 8.0" ,  "8.0", evaluator.evaluate("12/1.5"));
        assertEquals("Should evaluate to 9.0" ,  "9.0", evaluator.evaluate("13.5/1.5"));

        assertEquals("Should evaluate to 4.0", "4.0", evaluator.evaluate("-.0+2x6/3"));
        assertEquals("Should evaluate to 4.0", "4.0", evaluator.evaluate("-0.0+2x6/3"));
        assertEquals("Should evaluate to 4.0", "4.0", evaluator.evaluate("1.5x8/3"));
        assertEquals("Should evaluate to 5.0", "5.0", evaluator.evaluate("1+1.5x8/3"));
        assertEquals("Should evaluate to 6.1", "6.1", evaluator.evaluate("1.1+2x6/3+1"));
        assertEquals("Should evaluate to 2.0", "2.0", evaluator.evaluate("1+0.0x6/3+1"));
    }

    public void testZeroAsInputExpression() throws EvaluationException {
        Evaluator evaluator = new Evaluator();
        assertEquals("Should return 0 for 0 as input", "0", evaluator.evaluate("0"));
        assertEquals("Should return 0 for 0 as input", "0", evaluator.evaluate("0"));
        assertEquals("Should return 0.0 for 0.0 as input", "0.0", evaluator.evaluate("0.0"));
    }

    public void testDivideByZero(){
        assertThrowsEvaluationException("1/0");
        assertThrowsEvaluationException("1/.0");
        assertThrowsEvaluationException("1/0.0");
        assertThrowsEvaluationException("1+3/0");
        assertThrowsEvaluationException("1+3/.0");
        assertThrowsEvaluationException("1+3/0.0");
    }

    // some edge cases.
    public void testMalformedExpressions(){
        assertThrowsEvaluationException(null);
        assertThrowsEvaluationException("");
        assertThrowsEvaluationException("1+");
        assertThrowsEvaluationException("1-");
        assertThrowsEvaluationException("1++2");
        assertThrowsEvaluationException("1//2");
        assertThrowsEvaluationException("1+2**");
        assertThrowsEvaluationException("1+2**4");
        assertThrowsEvaluationException("1+2xx");
        assertThrowsEvaluationException("1+2xx4");
        assertThrowsEvaluationException("-+1");
        assertThrowsEvaluationException("-+1+2");
        assertThrowsEvaluationException("1//");
        assertThrowsEvaluationException("1/2//");
        assertThrowsEvaluationException("1/4//4");
    }

    protected void assertThrowsEvaluationException(String exp){
        Evaluator evaluator = new Evaluator();
        try {
            evaluator.evaluate(exp);
            fail("The expression " + exp + " should throw EvaluationException.");
        } catch (EvaluationException e) {
            return;
        }

    }
}

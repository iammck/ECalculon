package com.mck.ecalculon.evaluator;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mike on 5/3/2015.
 */
public class Evaluator {
    // currently finding and using the max dec amount from input.
    private int maxDecimalCount;
    private int significantDigits;

    public String evaluate(String expression) throws EvaluationException {
        if (expression == null){
            throw new EvaluationException("Expression was null.");
        }
        maxDecimalCount = -1;
        significantDigits = 0;
        ArrayList<Symbol> resultList = new ArrayList<Symbol>();
        ArrayList<Symbol> pendingList= new ArrayList<Symbol>();
        ArrayList<String> exprList = getExpressionAsArrayList(expression);
        Log.v("com.mck.dec", "Evaluator exprList " + exprList);
        boolean lastExprWasNumber = false;
        for(String item: exprList){
            // can we use a string for a switch statement?
            switch (item){
                case ("+"):
                    new Addition(resultList, pendingList);
                    break;
                case ("-"):
                    // subtraction.
                    if (lastExprWasNumber == true ){
                        new Subtraction(resultList, pendingList);
                    } else { // unary negation
                        new Negation(resultList, pendingList);
                    }
                    break;
                case ("x"):
                    new Multiplication(resultList, pendingList);
                    break;
                case ("/"):
                    new Division(resultList, pendingList);
                    break;
                default : // Expecting a number.
                    new NumberSymbol(item, resultList, pendingList);
                    lastExprWasNumber = true;
                    continue;
            }
            lastExprWasNumber = false;
        }

        while (!pendingList.isEmpty()) {
            // get the last item in pending list and have it evaluated
            if (!pendingList.get(pendingList.size() - 1).evaluate()){
                // if unable to evaluate something in the pendingList throw exception
                throw new EvaluationException("The pendingList could not finish evaluating."
                + " There are still " + pendingList.size() + " items.");
            }
        }
        // if pending List is empty, result should be ready.
        // if there is not exactly one resultList item
        if (resultList.size() != 1){
            throw new EvaluationException("The expected resultList is not " +
                    "correct, expecting 1, but the size is " + resultList.size());
        }

        // Get the result as a string and update the decimal count
        String result = resultList.get(0).toString();
        // if this number is large or small
        if (result.contains("e") || result.contains("E")){
            return result;
        }
        Log.v("com.mck.dec", "Evaluator result is " + result);
        return toMaxDecimalCount(result);
    }

    /**
     * Returns the expression as an array list also updates the maxDecimalCount
     * and significantDigits for this this expression.
     * @param expression
     * @return
     */
    private ArrayList<String> getExpressionAsArrayList(String expression) {
        // the resulting array list of terms from the expression..
        ArrayList<String> result = new ArrayList<String>();
        // When looping, determines if currently building number.
        boolean isBuildingNumber = false;
        // When building numbers keep track of a decimal and decimal count.
        boolean hasDecimal = false;
        boolean lastWasExp = false;
        int decCount = 0;
        // for each character in the expression
        for(int index = 0; index < expression.length(); index++){
            // get the current char from the index.
            char currentChar = expression.charAt(index);
            if (isOperator(currentChar) && !lastWasExp){
                // add operator and stop building number.
                result.add(String.valueOf(currentChar));
                isBuildingNumber = false;
                // If there was a decimal, update decimal count.
                if( hasDecimal){
                    updateMaxDecimalCount(decCount);
                }
            } else { // must be a number part,
                // This is a new number if false.
                if (isBuildingNumber == false) {
                    // create the number in the result
                    result.add("");
                    // set has decimal and count for this number
                    hasDecimal = false;
                    decCount = -1;
                    // now building a number
                    isBuildingNumber = true;
                }
                // the last is no longer an E or e.
                lastWasExp = false;
                // if this number has a decimal
                if (hasDecimal) {
                    decCount++;
                // else if this is the decimal, start decimal count
                } else if (currentChar == '.') {
                    Log.v("com.mck.dec", "Found decimal");
                    decCount = 0;
                    hasDecimal = true;
                }
                // if this is the exp
                if (currentChar == 'E' || currentChar == 'e'){
                    Log.v("com.mck.exp", "Found exp");
                    lastWasExp = true;
                }
                // Add this char to the last index in result.
                int lastIndex = result.size() - 1;
                String original = result.get(lastIndex);
                result.set(lastIndex, original + currentChar);
            }
        }
        // if there was a decimal in the last steps
        if( hasDecimal){
            updateMaxDecimalCount(decCount);
        }
        Log.v("com.mck.dec", "maxDecimalCount found with value of " + maxDecimalCount);
        return result;
    }

    private void updateMaxDecimalCount(int decimalCount) {
        // if the new count is more, make update.
        if (maxDecimalCount < decimalCount){
            Log.v("com.mck.dec", "maxDecimalCount Update from "
                    + maxDecimalCount + " to " + decimalCount);
            maxDecimalCount = decimalCount;
        }
    }

    private String toMaxDecimalCount(String value){
        Log.v("com.mck.dec", "toMaxDecimalCount with value " + value
                + " and maxDecimalCount " + maxDecimalCount + ".");
        // If no decimal originally or there is only a '.' then remove it
        if ( maxDecimalCount <= 0 && value.contains(".")){
            // if the decimal numbers are not equal to 0 or 00, keep some decimal.
            int index = value.indexOf('.');
            // if the value doesn't contain .00, but does have 2 chars after index
            if(!value.contains(".00") && index + 2 < value.length() ){
                // some decimal placeses were created, ie 2/3 = 0.66
                return value.substring(0, index + 3);
            // else is there at least one char past decimal not a zero.
            } else if (!value.contains(".0") && index + 1 < value.length()){
                return value.substring(0, index + 2);
            }
            // get a substring without the .0 and return it.
            return value.substring(0, value.indexOf('.'));
        // else, if there is no decimal.
        } else if ( maxDecimalCount < 0){
            return value;
        }

        // get the period index then last char position, (0-start index).
        int periodIndex = value.indexOf('.');
        int lastCharPosition = periodIndex + maxDecimalCount;
        // If value length is longer than lastCharPosition + 1
        if (lastCharPosition + 1 < value.length() ) {
            // Return the rounded number using the extra position.
            return round(value.substring(0, lastCharPosition + 2 ));
        }
        // values decimal count is less than max, return value.
        return value;
    }

    private String round(String value) {
        Log.v("com.mck.dec", "Value " + value + " is being rounded.");
        StringBuilder result = new StringBuilder();
        // get the last index
        int lastIndex = value.length() - 1;
        // if last is under 5
        if (value.charAt(lastIndex) < '5') {
            // return the substring minus the last position
            return value.substring(0, lastIndex);
        }
        // move last index
        lastIndex--;
        // start off having not rounded..
        boolean hasRounded = false;
        // working through the value in reverse
        while (lastIndex > -1) {
            // is this index the decimal or negation?
            if (value.charAt(lastIndex) < '.') {
                result.append('.');
            } else if (value.charAt(lastIndex) < '-') {
                result.append('-');
            } else if (!hasRounded){
                // if last index data is '9', make it '0'
                if (value.charAt(lastIndex) == '9') {
                    result.append('0');
                } else {
                    // add 1 to the last index
                    char ch = value.charAt(lastIndex);
                    int numb = ch + 1;
                    // add to result
                    result.append( (char) numb );
                    //  having rounded.
                    hasRounded = true;
                }
            } else { // already rounded,
                // add the lastIndex data to result.
                result.append(value.charAt(lastIndex));
            }// move to the previous index.
            lastIndex--;
        }
        // for a "9.9" if not has rounded, then append a 1
        if(!hasRounded){
            result.append('1');
        }
        result.reverse();
        Log.v("com.mck.dec", "Value " + value + " was rounded to " + result.toString() + ".");
        // reverse and return the result.
        return result.toString();
    }


    private boolean isOperator(char currentChar) {
        if (currentChar == '-' || currentChar == '+' || currentChar == '/' || currentChar == 'x' ){
            return true;
        }
        return false;
    }
}

package com.mck.ecalculon.evaluator;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mike on 5/3/2015.
 */
public class Evaluator {
    public String evaluate(String expression) throws EvaluationException {
        if (expression == null || expression.isEmpty()){
            throw new EvaluationException("The pendingList could not finish evaluating.");
        }

        ArrayList<Symbol> resultList = new ArrayList<Symbol>();
        ArrayList<Symbol> pendingList = new ArrayList<Symbol>();
        ArrayList<String> expressionList = getExpressionArrayList(expression);
        boolean isSubtract = false;

        for (String item : expressionList) {
            // can we use a string for a switch statement?
            switch (item) {
                case ("+"):
                    new Addition(resultList, pendingList);
                    break;
                case ("-"):
                    if( isSubtract){
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
                case ("("):
                    new LeftParenthesis(resultList, pendingList);
                    break;
                case (")"):
                new RightParenthesis(resultList, pendingList);
                isSubtract = true;
                continue;
                default: // Expecting a number.
                    new NumberSymbol(item, resultList, pendingList);
                    isSubtract = true;
                    continue;
            }
            isSubtract = false;
        }

        while (!pendingList.isEmpty()) {
            // get the last item in pending list and have it evaluated
            if (!pendingList.get(pendingList.size() - 1).evaluate()) {
                // if unable to evaluate something in the pendingList throw exception
                throw new EvaluationException("The pendingList could not finish evaluating."
                        + " There are still " + pendingList.size() + " items.");
            }
        }
        // if pending List is empty, result should be ready.
        // if there is not exactly one resultList item
        if (resultList.size() != 1) {
            throw new EvaluationException("The expected resultList is not " +
                    "correct, expecting 1, but the size is " + resultList.size());
        }

        // Get the result as a string
        String result = resultList.get(0).toString();
        // if this number is large or small
        if (result.contains("e") || result.contains("E")) {
            return result; // do not worry about rounding
        }
        result = getRoundedNumber(result, expressionList);
        return result; // toMaxDecimalCount(result);
    }

    public ArrayList<String> getExpressionArrayList(String expression) throws EvaluationException {
        String[] expressionArray = expression.split(" ");
        ArrayList<String> result = new ArrayList<String>();
        for (int index = 0; index < expressionArray.length; index++) {
            result.add(expressionArray[index]);
        }
        return result;
    }

    private String getRoundedNumber(String number, ArrayList<String> expressionList) {
        int accuracy = getMaxAccuracy(expressionList);
        Log.v("com.mck.rounding", "getRoundedNumber for number " + number + " with accuracy " + accuracy);
        String result = getNumberWithAccuracy(number, accuracy);
        Log.v("com.mck.rounding", "getRoundedNumber returning " + result);
        return result;
    }

    private String getNumberWithAccuracy(String number, int accuracy) {
        // If accuracy is 0 and number has a '.', want to keep a few digits. ie 2/3
        if ( accuracy == 0 && number.contains(".")){
            int index = number.indexOf('.');
            Log.v("com.mck.rounding", "getNumberWithAccuracy with number " + number
                    + " and index " + index);
            // if the value has 3 chars after index, return two
            if( index + 3 < number.length() ){
                if (number.contains(".000")){
                    return number.substring(0,index);
                } else {
                    return round(number.substring(0, index + 4));
                }
            // else is there at least 2 char past decimal not a zero.
            } else if (index + 2 < number.length()){
                if (number.contains(".00")){
                    return number.substring(0,index);
                } else {
                    return round(number.substring(0, index + 3));
                }
            // else is there at least 1 char past decimal not a zero.
            } else if (index + 1 < number.length()){
                if (number.contains(".0")){
                    return number.substring(0,index);
                } else {
                    return number.substring(0, index + 2);
                }
            } else { // return no decimal either.
                return number.substring(0, index);
            }
        // else, if accuracy is 0, but no '.'
        } else if ( accuracy == 0){
            return number;
        // else, if there is accuracy, but no decimal
        } else if (!number.contains(".")){
            StringBuilder builder = new StringBuilder();
            builder.append(number).append(".");
            for( int i = 0; i < accuracy; i++){
                builder.append("0");
            }
            return builder.toString();
        }

        // The last position from '.' includes the accuracy plus one for the
        // rounding position and one for index inclusion.
        int lastPosition = number.indexOf('.') + accuracy + 2;
        // If lastPosition is less than length
        if (lastPosition < number.length() ) {
            // Return the rounded number from the substring.
            return round (number.substring(0, lastPosition));
        } else { //the last char position is greater or equal to number length.
            return number;
        }
    }

    private int getMaxAccuracy(ArrayList<String> expressionList) {
        int result = 0;
        for(String number : expressionList){
            if (number.contains(".")){
                int index = number.indexOf('.');
                int accuracy = number.length() - 1 - index;
                result = (result > accuracy)? result: accuracy;
            }
        }
        return result;
    }

    private String round(String value) {
        Log.v("com.mck.rounding", "round with value " + value);
        // get the last index
        int index = value.length() - 1;
        // if last is under 5
        if (value.charAt(index) < '5') {
            // return the substring minus the last position
            return value.substring(0, index);
        }
        // Keep track of the rounded result.
        StringBuilder result = new StringBuilder();
        // keep track of when rounding has finished..
        boolean finishedRounding = false;
        // move index and
        index--;
        // while working through the indexed values in reverse
        while (index > -1) {
            Log.v("com.mck.rounding", "while loop starting round result value is " + result.toString());
            // if indexed value is a the decimal?
            if (value.charAt(index) < '.') {
                result.append('.');
            // or negation,
            } else if (value.charAt(index) < '-') {
                result.append('-');
            // or if have not yet rounded, round it.
            } else if (!finishedRounding){
                // if index value is '9', make it '0'
                if (value.charAt(index) == '9') {
                    result.append('0');

                } else {
                    // add 1 to the last index
                    char ch = value.charAt(index);
                    int numb = ch + 1;
                    // add to result
                    result.append( (char) numb );
                    //  having rounded.
                    finishedRounding = true;
                }

            // Already rounded.
            } else {  // add the lastIndex data to result.
                result.append(value.charAt(index)); //
            }// move to the previous index.
            index--;
        }
        // for a "9.9" if not has rounded, then append a 1
        if(!finishedRounding){
            result.append('1');
        }
        result.reverse();

        Log.v("com.mck.rounding", "round result is now " + result.toString());
        // reverse and return the result.
        return result.toString();
    }

}
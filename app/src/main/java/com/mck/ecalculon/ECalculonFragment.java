package com.mck.ecalculon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mck.ecalculon.evaluator.EvaluationException;
import com.mck.ecalculon.evaluator.Evaluator;

import java.security.InvalidParameterException;

/**
 * Created by mike on 5/2/2015.
 */
public class ECalculonFragment extends Fragment {

    private static final java.lang.String KEY_L_PARENTHESIS_COUNT = "KEY_L_PARENTHESIS_COUNT";
    private static final java.lang.String KEY_R_PARENTHESIS_COUNT = "KEY_R_PARENTHESIS_COUNT";
    private static final java.lang.String KEY_CURRENT_EXPRESSION = "KEY_CURRENT_EXPRESSION";
    private OutputFragment outputFragment;

    private String currentExpression;
    private int lParenthesisCount;
    private int rParenthesisCount;

    public ECalculonFragment(){
        currentExpression = "0";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.ecalculon_fragment, container, false);
        // no saved state, set up the fragments
        if (savedInstanceState == null){
            // No saved state, set up local variables to defaults.
            lParenthesisCount = 0;
            rParenthesisCount = 0;
            currentExpression = "";
            // Add the fragments to a private Fragement Manager
            OutputFragment output = new OutputFragment();
            InputFragment input = new InputFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.output_fragment_container, output)
                    .add(R.id.input_fragment_container, input)
                    .commit();
        } else {
            lParenthesisCount = savedInstanceState.getInt(KEY_L_PARENTHESIS_COUNT, 0);
            rParenthesisCount = savedInstanceState.getInt(KEY_R_PARENTHESIS_COUNT, 0);
            currentExpression = savedInstanceState.getString(KEY_CURRENT_EXPRESSION);
        }
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_L_PARENTHESIS_COUNT, lParenthesisCount);
        outState.putInt(KEY_L_PARENTHESIS_COUNT, lParenthesisCount);
        outState.putString(KEY_CURRENT_EXPRESSION, currentExpression);
    }

    public String getCurrentExpression() {
        return currentExpression;
    }

    public void insertNumber(char number) {
        if(hasMaxOutput()){
            return;
        }
        currentExpression += String.valueOf(number);
        getOutputFragment().setOutput(currentExpression);
    }

    public void insertDecimal() {
        if(hasMaxOutput()){
            return;
        }
        // if currentExpression is empty or the last item is a ' '
        if (currentExpression.isEmpty() ||
                currentExpression.charAt(currentExpression.length() - 1) == ' '){
            currentExpression += "0";
        }
        currentExpression += ".";
        getOutputFragment().setOutput(currentExpression);
    }

    public void insertOperator(char operator) {
        if(hasMaxOutput()){
            return;
        }
        // if current expression isn't empty and the last item is a '.', insert a trailing '0'
        if (!currentExpression.isEmpty() &&
                currentExpression.charAt(currentExpression.length() -1) == '.'){

            insertNumber('0');
        }
        currentExpression += " " + String.valueOf(operator) + " ";
        getOutputFragment().setOutput(currentExpression);
    }


    public void insertNegation() {
        if(hasMaxOutput()){
            return;
        }
        // the leading operator, if any should insert the left space
        currentExpression += String.valueOf("- "); // neg + right space.
        getOutputFragment().setOutput(currentExpression);
    }

    public void insertLeftParenthesis() {
        if(hasMaxOutput()){
            return;
        }
        lParenthesisCount++;

        currentExpression += "( ";
        getOutputFragment().setOutput(currentExpression);
    }

    public void insertRightParenthesis() {
        if(hasMaxOutput()){
            return;
        }
        rParenthesisCount++;

        if (!currentExpression.isEmpty()){
            char last = currentExpression.charAt(currentExpression.length() -1);
            // if current expression isn't empty and the last item is a '.', insert a trailing '0'
            if (last == '.'){
                insertNumber('0');
            }
        }
        currentExpression += " )";
        getOutputFragment().setOutput(currentExpression);
    }

    public void undo() {
        Log.v("com.mck.undo", "entering undo");
        if (currentExpression.isEmpty()){
            getOutputFragment().setOutput("0");
            return;
        }
        char lastChar = currentExpression.charAt(currentExpression.length() - 1);
        Log.v("com.mck.undo", "lastChar is '" + lastChar + "'");

        // if the last is not a ')' and not a ' ', remove it, but be a number or decimal.
        if(lastChar != ')' && lastChar != ' '){ // remove it.
            Log.v("com.mck.undo", "last was not ')' and not ' ', but was " + lastChar);
            currentExpression = currentExpression.substring(
                    0,currentExpression.length() - 1);
        // if the second to last is a negation or parenthesis remove last 2 indices.
        } else if (wasNegationLastInput()) {
            Log.v("com.mck.undo", "last was negation");
            currentExpression = currentExpression.substring(
                    0, currentExpression.length() - 2);
            // remove the operator plus its spacing.
        }else if (getLastInput() == '('){
            Log.v("com.mck.undo", "last was lparenthesis");
            lParenthesisCount--;
            currentExpression = currentExpression.substring(
                    0,currentExpression.length() - 2);
        }else if (getLastInput() == ')'){
            Log.v("com.mck.undo", "last was rParenthesis");
            rParenthesisCount--;
            currentExpression = currentExpression.substring(
                    0,currentExpression.length() - 2);
        } else { // must be operator, remove 3 indices.
            Log.v("com.mck.undo", "last was operator");
            currentExpression = currentExpression.substring(
                    0,currentExpression.length() - 3);
        }
        // set the output to 0 if currentExpression is empty.
        String output = currentExpression.isEmpty()? "0": currentExpression;
        getOutputFragment().setOutput(output);
    }

    private boolean wasNegationLastInput() {
        char last = getLastInput();
        if (last == '-'){
            // is currentExpression a "- "
            if (currentExpression.length() == 2){
                return true;

            }

            char secondLast = getSecondToLastInput();
            Log.v("com.mck.undo", "wasNegationLastInput found last, secondLast as "
                    + last + ", " + secondLast);
            if ( secondLast == '-' ||
                    secondLast == '+' ||
                    secondLast == 'x' ||
                    secondLast == '/' ||
                    secondLast == '('){

                return true;
            }
        }
        return false;
    }

    public void clear() {
        currentExpression = "";
        lParenthesisCount = 0;
        rParenthesisCount = 0;
        getOutputFragment().setOutput("");
        getOutputFragment().setOutput("0");
    }

    public void evaluate() {
        try {
            if (currentExpression.isEmpty()){
                currentExpression = "0";
            }
            Evaluator evaluator = new Evaluator();
            currentExpression = evaluator.evaluate(currentExpression);
            getOutputFragment().setOutput(currentExpression);
        } catch (EvaluationException e) {
            e.printStackTrace();
            currentExpression = "";
            getOutputFragment().setError();
        } finally {
            lParenthesisCount = 0;
            rParenthesisCount = 0;
        }
    }


    public int getParenthesisDifference() {
        return lParenthesisCount - rParenthesisCount;
    }


    private boolean hasMaxOutput() {
        return getOutputFragment().hasMaxOutput();
    }

    public OutputFragment getOutputFragment(){
        if (outputFragment == null) {
            outputFragment = (OutputFragment) getChildFragmentManager()
                    .findFragmentById(R.id.output_fragment_container);
        }
        return outputFragment;
    }


    public char getLastInput() {
        if (currentExpression.isEmpty()){
            throw new InvalidParameterException("currentExpression is empty!");
        }
        char result = currentExpression.charAt(currentExpression.length() - 1);
        if (result == ' '){
            result = currentExpression.charAt(currentExpression.length() - 2);
        }
        return result;
    }

    public char getSecondToLastInput() {
        if (currentExpression.isEmpty() || currentExpression.length() < 2){
            throw new InvalidParameterException("currentExpression is not at least two in length!");
        }

        char last = currentExpression.charAt(currentExpression.length() - 1);
        int firstIndex = (last == ' ') ?
            currentExpression.length() - 2 : currentExpression.length() -1;

        char secondLast = currentExpression.charAt((firstIndex - 1));
        if (secondLast == ' '){
            secondLast = currentExpression.charAt((firstIndex - 2));
        }
        return secondLast;
    }
}
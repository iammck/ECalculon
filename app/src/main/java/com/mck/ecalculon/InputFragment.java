package com.mck.ecalculon;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.InvalidParameterException;

/**
 *
 * Created by mike on 5/2/2015.
 */
public class InputFragment extends Fragment implements View.OnClickListener {

    private static final java.lang.String KEY_WAS_LAST_BUTTON_EQUALS = "KEY_WAS_LAST_BUTTON_EQUALS" ;
    private boolean wasLastButtonEquals = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.input_fragment, container, false);
        // set this as the onClickListener for the buttons.
        result.findViewById(R.id.buttonDecimal).setOnClickListener(this);
        result.findViewById(R.id.button0).setOnClickListener(this);
        result.findViewById(R.id.button1).setOnClickListener(this);
        result.findViewById(R.id.button2).setOnClickListener(this);
        result.findViewById(R.id.button3).setOnClickListener(this);
        result.findViewById(R.id.button4).setOnClickListener(this);
        result.findViewById(R.id.button5).setOnClickListener(this);
        result.findViewById(R.id.button6).setOnClickListener(this);
        result.findViewById(R.id.button7).setOnClickListener(this);
        result.findViewById(R.id.button8).setOnClickListener(this);
        result.findViewById(R.id.button9).setOnClickListener(this);

        result.findViewById(R.id.buttonEquals).setOnClickListener(this);

        result.findViewById(R.id.buttonDivision).setOnClickListener(this);
        result.findViewById(R.id.buttonMultiplication).setOnClickListener(this);
        result.findViewById(R.id.buttonAdd).setOnClickListener(this);
        result.findViewById(R.id.buttonMinus).setOnClickListener(this);

        result.findViewById(R.id.buttonClear).setOnClickListener(this);
        result.findViewById(R.id.buttonUndo).setOnClickListener(this);

        result.findViewById(R.id.buttonPi).setOnClickListener(this);
        result.findViewById(R.id.buttonLeftParenthesis).setOnClickListener(this);
        result.findViewById(R.id.buttonRightParenthesis).setOnClickListener(this);

        result.findViewById(R.id.button1).requestFocus();

        if (savedInstanceState != null){
            wasLastButtonEquals = savedInstanceState.getBoolean(KEY_WAS_LAST_BUTTON_EQUALS, false);
        } else {
            wasLastButtonEquals = false;
        }
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_WAS_LAST_BUTTON_EQUALS, wasLastButtonEquals);
    }

    @Override
    public void onClick(View view) {
        ECalculonFragment parentFragment = (ECalculonFragment) getParentFragment();
        char nextChar = ((TextView) view).getText().charAt(0);

        switch (view.getId()) {
            case (R.id.button0):
            case (R.id.button1):
            case (R.id.button2):
            case (R.id.button3):
            case (R.id.button4):
            case (R.id.button5):
            case (R.id.button6):
            case (R.id.button7):
            case (R.id.button8):
            case (R.id.button9):
                handlePreviousEquals();
                if ( validNumberInsert()){
                    parentFragment.insertNumber(nextChar);
                }
                break;
            case (R.id.buttonAdd):
            case (R.id.buttonDivision):
            case (R.id.buttonMultiplication):
                wasLastButtonEquals = false;
                if (validBinaryOperatorInsert()){
                    parentFragment.insertOperator(nextChar);
                }
                break;
            case (R.id.buttonMinus):
                wasLastButtonEquals = false;
                if (validBinaryOperatorInsert()){
                    parentFragment.insertOperator(nextChar);
                } else if (validNegationInsert()) {
                    parentFragment.insertNegation();
                }
                break;
            case (R.id.buttonUndo):
                handlePreviousEquals();
                parentFragment.undo();
                break;
            case (R.id.buttonClear):
                wasLastButtonEquals = false;
                parentFragment.clear();
                break;
            case (R.id.buttonEquals):
                wasLastButtonEquals = true;
                parentFragment.evaluate();
                break;
            case (R.id.buttonDecimal):
                handlePreviousEquals();
                if (validDecimalInsert()){
                    parentFragment.insertDecimal();
                }
                break;
            case(R.id.buttonPi):
                handlePreviousEquals();
                if (validDecimalInsert()){
                    parentFragment.insertNumber('3');
                    parentFragment.insertDecimal();
                    parentFragment.insertNumber('1');
                    parentFragment.insertNumber('4');
                }
                break;
            case(R.id.buttonLeftParenthesis):
                handlePreviousEquals();
                if (validLeftParenthesisInsert()){
                    parentFragment.insertLeftParenthesis();
                }
                break;
            case(R.id.buttonRightParenthesis):
                handlePreviousEquals();
                if (validRightParenthesisInsert()){
                    parentFragment.insertRightParenthesis();
                }
                break;
        }
    }

    private boolean validRightParenthesisInsert() {
        ECalculonFragment parentFragment = (ECalculonFragment) getParentFragment();
        String currentExpression = parentFragment.getCurrentExpression();
        if (currentExpression.isEmpty() ||
                parentFragment.getParenthesisDifference() == 0){

            return false;
        }
        char lastChar = parentFragment.getLastInput();
        // if the last char is not a +,-,x,/, or (, then valid.
        if (lastChar != 'x' && lastChar != '/' && lastChar != '-' &&
                lastChar != '+' && lastChar != '(' ){
            return true;
        }
        return false;
    }

    private boolean validLeftParenthesisInsert() {
        ECalculonFragment parentFragment = (ECalculonFragment) getParentFragment();
        String currentExpression = parentFragment.getCurrentExpression();
        if (currentExpression.isEmpty()){
            return true;
        }
        char lastChar = parentFragment.getLastInput();
        // if the last char is a +,-,x,/,(
        if (lastChar == 'x' || lastChar == '/' || lastChar == '-' ||
                lastChar == '+' || lastChar == '(' ){
            return true;
        }
        return false;
    }

    private boolean validDecimalInsert() {
        ECalculonFragment parentFragment = (ECalculonFragment) getParentFragment();
        String currentExpression = parentFragment.getCurrentExpression();
        // must also be a valid number insert. and last number must not have decimal
        if (!validNumberInsert() || lastNumberHasDecimal(currentExpression)){
            return false;
        }
        return true;
    }

    private boolean validNegationInsert() {
        ECalculonFragment parentFragment = (ECalculonFragment) getParentFragment();
        String currentExpression = parentFragment.getCurrentExpression();
        if (currentExpression.isEmpty()){
            return true;
        }
        char lastChar = parentFragment.getLastInput();
        // if the lastChar is one of  +, x, /, or (
        if (lastChar == '+' || lastChar == 'x' || lastChar == '/' || lastChar == '('){
            return true;
            // else if lastChar was minus symbol
        } else if (lastChar == '-'){
            // if the current expression is just the minus symbol,
            if (currentExpression.length() == 2){
                return false; // already a negation.
            }
            // otherwise check the next to last item for operator.
            char secondLast = parentFragment.getSecondToLastInput();
            if (secondLast == '-' || secondLast == '+' || secondLast == 'x' ||
                    secondLast == '/' || secondLast == '(' ||secondLast == ')') {
                return false; // already a negation
            }
            return true;
        }
        return false;
    }

    private boolean validBinaryOperatorInsert() {
        ECalculonFragment parentFragment = (ECalculonFragment) getParentFragment();
        String currentExpression = parentFragment.getCurrentExpression();
        if (currentExpression.isEmpty()){
            return false;
        }
        char lastChar = parentFragment.getLastInput();
        // if the last char is not one of  x,/,+,-,(
        if (lastChar != 'x' &&  lastChar != '/' &&
                lastChar != '+' && lastChar != '-' && lastChar != '(' ){

            return true;
        }
        return false;

    }

    private void handlePreviousEquals(){
        if (wasLastButtonEquals){
            wasLastButtonEquals = false;
            ECalculonFragment parentFragment = (ECalculonFragment) getParentFragment();
            parentFragment.clear();
        }

    }

    private boolean validNumberInsert() {
        ECalculonFragment parentFragment = (ECalculonFragment) getParentFragment();
        String currentExpression = parentFragment.getCurrentExpression();
        // if the expr is empty or last char is not ')', insert
        if (currentExpression.isEmpty() || parentFragment.getLastInput() != ')'){
            return true;
        }
        return false;
    }

    private boolean lastNumberHasDecimal(String currentExpression) {
        if (currentExpression.isEmpty()){
            return false;
        }
        String[] expressions = currentExpression.split(" ");
        String lastExpression = expressions[expressions.length - 1];
        if (lastExpression.contains(".")){
            return true;
        }
        return false;
    }

}
package com.mck.ecalculon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 5/2/2015.
 */
public class ECalculonFragment extends Fragment {

    private static final String KEY_NUMBERS = "MainActivity.KEY_NUMBERS";
    private static final String KEY_OPERATORS = "MainActivity.KEY_OPERATORS";
    private static final String KEY_INPUTS = "MainActivity.KEY_INPUTS" ;
    private Evaluator evaluator;
    private ArrayList<String> numbers;
    private ArrayList<String> operators;
    private boolean wasError = false;
    private OutputFragment outputFragment;
    private ArrayList<InputTypes> inputHistory;
    private enum InputTypes {
        number,operator;
        public static ArrayList<InputTypes> fromStringArrayList(ArrayList<String> input){
            ArrayList<InputTypes> result = new ArrayList<InputTypes>();
            for(String type: input){
                result.add(InputTypes.valueOf(type));
            }
            return result;
        }
        public static ArrayList<String> toStringArrayList(ArrayList<InputTypes> input){
            ArrayList<String> result = new ArrayList<String>();
            for(InputTypes type: input){
                result.add(type.toString());
            }
            return result;
        }
    }

    public ECalculonFragment(){
        evaluator = new Evaluator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.ecalculon_fragment,container, false);
        // no saved state, set up the fragments
        if (savedInstanceState == null){
            // No saved state, set up local variables to defaults.
            numbers = new ArrayList<String>();
            numbers.add("0");
            operators = new ArrayList<String>();
            inputHistory = new ArrayList<InputTypes>();

            // Add the fragments to a private Fragement Manager
            OutputFragment output = new OutputFragment();
            InputFragment input = new InputFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.output_fragment_container, output)
                    .add(R.id.input_fragment_container, input)
                    .commit();
        } else {
            // there is a saved instance state, use it to setup variables.
            numbers = savedInstanceState.getStringArrayList(KEY_NUMBERS);
            operators = savedInstanceState.getStringArrayList(KEY_OPERATORS);
            ArrayList<String> inputsAsStrings = savedInstanceState.getStringArrayList(KEY_INPUTS);
            inputHistory = InputTypes.fromStringArrayList(inputsAsStrings);

        }
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_NUMBERS, numbers);
        outState.putStringArrayList(KEY_OPERATORS, operators);
        ArrayList<String> inputsAsStrings  = InputTypes.toStringArrayList(inputHistory);
        outState.putStringArrayList(KEY_INPUTS, inputsAsStrings);
    }

    public void handleNumberInput(String number) {
        if(hasMaxOutput()){
            return;
        }
        // if last item in numbers is just a "0", replace it.
        if (numbers.get(numbers.size() - 1).equals("0")){
            numbers.set(numbers.size() - 1, number);
        } else { // else append this number.
            String current = numbers.get(numbers.size() - 1);
            numbers.set(numbers.size() - 1, current + number);
        }

        if (inputHistory.size() == 0 || wasError) {
            getOutputFragment().setOutput(number);
            wasError = false;
        } else {
            getOutputFragment().appendString(number);
        }
        // do I need to add  a new instance?
        inputHistory.add(InputTypes.number);
    }

    public void handleOperatorInput(String operator) {
        if(hasMaxOutput()){
            return;
        }// Get previous input type it is either null or the last one recorded.
        int hSize = inputHistory.size();
        InputTypes prevType = (hSize == 0 )? null: inputHistory.get(hSize -1);

        // The operator is binary if prev input type is a number
        if ( prevType != null && prevType == InputTypes.number){
            // add numbers entry, update operators and input history
            numbers.add("0");
            operators.add(operator);
            inputHistory.add(InputTypes.operator);
            // update the output fragment
            getOutputFragment().appendString(operator);
        } else {
            // if the operator is not binary, it may still be unary negation.
            if (operator.equals("-")) {
                // do not insert a new numbers item, but do other updates.
                operators.add(operator);
                inputHistory.add(InputTypes.operator);
                // If this is the first item, set output to blank line
                if (prevType == null){ // and set wasError to false.
                    wasError = false;
                    getOutputFragment().setOutput("");

                }
                // add the operatory
                getOutputFragment().appendString(operator);
            }
        }
    }

    public void handleUndoInput(){
        // if there is no input history
        if (inputHistory.size() == 0 ){
            // just update the output and reset stuff.
            getOutputFragment().setOutput("0");
            numbers = new ArrayList<String>();
            numbers.add("0");
            operators = new ArrayList<String>();
            wasError = false;
            // nothing else to do, return.
            return;
        } // there is at least one input to undo.
        // depending on input history type.
        InputTypes last = inputHistory.get(inputHistory.size() - 1);
        if( last == InputTypes.number) {
            // the original number
            String original = numbers.get(numbers.size() - 1);
            // index of last element
            int lastIndex = original.length() - 1;
            String modified = null;
            // if it is a size of one, then set modified to "0";
            if (original.length() == 1) {
                modified = "0";
                // else if original length is two and is equal to "0."
            } else if (original.length() == 2 && original.equals("0.")) {
                // Check to see if the user entered the leading zero.
                // if input history is at least two and second to last input was a number
                if (inputHistory.size() > 1
                        && inputHistory.get(inputHistory.size() - 2) == InputTypes.number) {
                    //  remove a number input type from history
                    inputHistory.remove(inputHistory.size() - 2);
                }
                // leave the left most number for numbers list update.
                int i = original.length() - 1;
                modified = original.substring(0, i);

            }else { // index to be removed.
                int i = original.length() - 1;
                modified = original.substring(0, i);
            }
            numbers.set(numbers.size() - 1, modified);

            // remove last char from from the outputFragment
            getOutputFragment().removeLastChar();
            // If removing a period and there is more than one item in numbers.
            lastIndex = original.length() - 1;
            if (original.charAt(lastIndex) == '.' && numbers.size() > 1){
                // if original is length of two and original.charAt 0 is 0
                if (original.length() == 2 && original.charAt(0) == '0'){
                    // remove it from outputFragment
                    getOutputFragment().removeLastChar();
                }
            }
        } else if ( last == InputTypes.operator) {
            // if not (only one input history item,
            // or the second to last item type is also an operator)
            if (!(inputHistory.size() == 1
                    || (inputHistory.get(inputHistory.size() - 2)
                    == InputTypes.operator))) {
                // must be a binary operator remove last numbers index
                numbers.remove(numbers.size()-1);
            }
            // remove the last operator from operators,
            operators.remove(operators.size() - 1);
            // remove the last char from outputFrag
            getOutputFragment().removeLastChar();
            // if this was the first input in history
            if (inputHistory.size() == 1){
                // show a 0
                getOutputFragment().setOutput("0");
            }
        }
        // remove the input from the history.
        inputHistory.remove(inputHistory.size() - 1);

        // if the input history is now 0, set output to 0
        if( inputHistory.size() == 0 ) {
            getOutputFragment().setOutput("0");
        }
    }

    public void handleClearInput(){
        getOutputFragment().removeLastChar();
        getOutputFragment().setOutput("0");
        numbers.clear();
        numbers.add("0");
        operators.clear();
        inputHistory.clear();
    }

    public void handleEqualsInput() {
        OutputFragment outFrag = (OutputFragment) getChildFragmentManager()
                .findFragmentById(R.id.output_fragment_container);
        String expression = outFrag.getOutputString();
        evaluator.evaluate(expression);
        Log.v("com.mck", "handleEqualsInput ");
    }

    public void handleDecimalInput() {
        if(hasMaxOutput()){
            return;
        }
        if (hasDecimal()){
            return;
        }
        // get update string for output fragment
        String outFragUpdate = ".";
        // if this is not the first item in numbers.
        if (!(numbers.size() == 1)) {
            // if last numbers item is just a zero
            if (numbers.get(numbers.size() - 1).equals("0")) {
                // if the user did not enter the zero, then need to add the 0
                if (inputHistory.get(inputHistory.size() - 1) != InputTypes.number) {
                    // set the update for output fragment to "0."
                    outFragUpdate = "0.";
                }
            }
            // this is the first item in numbers, possibly clear error output
        } else {
            if (wasError){
                wasError = false;
                getOutputFragment().setOutput("0");
            }
        }
        // output the update with the output fragment
        getOutputFragment().appendString(outFragUpdate);

        // update the last numbers item with the decimal.
        String original = numbers.get(numbers.size() - 1);
        String update = original + ".";
        numbers.set(numbers.size() - 1, update);

        // add this input as a number input type into history.
        inputHistory.add(InputTypes.number);
    }

    private boolean hasMaxOutput() {
        return getOutputFragment().hasMaxOutput();
    }

    // helpers
    public List getNumbers() {
        return numbers;
    }

    public List getOperators() {
        return operators;
    }

    public List getInputTypeHistory() {
        return inputHistory;
    }

    public OutputFragment getOutputFragment(){
        if (outputFragment == null) {
            outputFragment = (OutputFragment) getChildFragmentManager()
                    .findFragmentById(R.id.output_fragment_container);
        }
        return outputFragment;
    }

    // testing helper methods.
    public boolean hasDecimal(){
        return numbers.get(numbers.size()-1).contains(".");
    }

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setWasError(Boolean value){
        wasError = true;
    }
}

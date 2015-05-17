package com.mck.ecalculon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mike on 5/2/2015.
 */
public class OutputFragment extends Fragment {
    private static final String KEY_OUTPUT = "OutputFragment.KEY_OUTPUT";
    private String currentOutput;
    private Integer maxOutputCharacters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.output_fragment,container, false);
        if (savedInstanceState != null){
            currentOutput = savedInstanceState.getString(KEY_OUTPUT);
            ((TextView) result.findViewById(R.id.output)).setText(currentOutput);
        } else {
            currentOutput = "0";
        }
        maxOutputCharacters = getResources().getInteger(R.integer.output_max_length);
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_OUTPUT, currentOutput);
    }

    public void appendString(String s) {
        if (getView() != null){
            TextView output = (TextView) getView().findViewById(R.id.output);
            output.append(s);
            currentOutput = output.getText().toString();
        }
    }

    public void setOutput(String s) {
        if (getView() != null){
            TextView output = (TextView) getView().findViewById(R.id.output);
            output.setText(s);
            currentOutput = output.getText().toString();
        }
    }

    public void removeLastChar() {
        if (getView() != null){
            TextView output = (TextView) getView().findViewById(R.id.output);
            String s = output.getText().toString();
            String result = s.substring(0, s.length()-1);
            output.setText(result);
            currentOutput = output.getText().toString();
        }
    }

    public String getOutputString() {
        return currentOutput;
    }

    public void clearError() {
        if (getView() != null){
            TextView output = (TextView) getView().findViewById(R.id.output);
            String s = "0";
            output.setText(s);
            currentOutput = output.getText().toString();
        }
    }

    public void setError() {
        if (getView() != null){
            TextView output = (TextView) getView().findViewById(R.id.output);
            String s = getResources().getString(R.string.error_output);
            output.setText(s);
            currentOutput = output.getText().toString();
        }
    }

    public boolean hasMaxOutput() {
        if (getView() != null) {
            TextView output = (TextView) getView().findViewById(R.id.output);
            int current = output.length();
            return (current >= maxOutputCharacters) ? true : false;
        }
        return false;
    }

}

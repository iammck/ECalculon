package com.mck.ecalculon;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.output_fragment,container, false);
        if (savedInstanceState != null){
            currentOutput = savedInstanceState.getString(KEY_OUTPUT);
            ((TextView) result.findViewById(R.id.output)).setText(currentOutput);
        } else {
            currentOutput = "0";
        }
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_OUTPUT, currentOutput);
    }

    public void setOutput(String s) {
        StringBuilder result = new StringBuilder();
        for(String str :s.trim().split(" ")){
            result.append(str);
        }

        currentOutput = result.toString();
        if (getView() != null){
            TextView output = (TextView) getView().findViewById(R.id.output);
            output.setText(currentOutput);
        }
    }

    public void setError() {
        currentOutput = "ERROR";
        if (getView() != null){
            TextView output = (TextView) getView().findViewById(R.id.output);
            output.setText(currentOutput);
        }
    }

    public boolean hasMaxOutput() {
        TextView output = (TextView) getView().findViewById(R.id.output);
        int currentLength = output.length();
        //max out is within two of max output length.
        int max = getResources().getInteger(R.integer.output_max_length);
        if (currentLength < max){
            return false;
        }
        return true;
    }
}

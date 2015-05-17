package com.mck.ecalculon;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

/**
 *
 * Created by mike on 5/2/2015.
 */
public class InputFragment extends Fragment implements View.OnClickListener {

    private MediaPlayer mPlayer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.input_fragment, container, false);
        // set this as the onClickListener for the buttons.
        result.findViewById(R.id.buttonDecimal).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber0).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber1).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber2).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber3).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber4).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber5).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber6).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber7).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber8).setOnClickListener(this);
        result.findViewById(R.id.buttonNumber9).setOnClickListener(this);

        result.findViewById(R.id.buttonEquals).setOnClickListener(this);

        result.findViewById(R.id.buttonDivision).setOnClickListener(this);
        result.findViewById(R.id.buttonMultiplication).setOnClickListener(this);
        result.findViewById(R.id.buttonAddition).setOnClickListener(this);
        result.findViewById(R.id.buttonSubtraction).setOnClickListener(this);

        result.findViewById(R.id.buttonClear).setOnClickListener(this);
        result.findViewById(R.id.buttonUndo).setOnClickListener(this);

        result.findViewById(R.id.buttonNumber1).requestFocus();
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        ECalculonFragment parentFragment = (ECalculonFragment) getParentFragment();

        switch (parent.getId()) {
            case (R.id.numberButtonLayout):
                parentFragment.handleNumberInput(((Button) view).getText().toString());
                break;
            case (R.id.basicOperatorButtonLayout):
                parentFragment.handleOperatorInput(((Button) view).getText().toString());
                break;
            case (R.id.menuButtonLayout):
                if (view.getId() == R.id.buttonUndo){
                    parentFragment.handleUndoInput();
                } else if (view.getId() == R.id.buttonClear) {
                    parentFragment.handleClearInput();
                }
                break;
            case (R.id.equalsButtonLayout):
                parentFragment.handleEqualsInput();
                break;
            case (R.id.decimalButtonLayout):
                parentFragment.handleDecimalInput();
        }

        /*if (mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(
                getActivity().getApplicationContext(), R.raw.keypress_standard);
        mPlayer.start();*/

    }
}

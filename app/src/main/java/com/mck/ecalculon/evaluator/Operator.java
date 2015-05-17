package com.mck.ecalculon.evaluator;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mike on 5/11/2015.
 */
public abstract class Operator extends Symbol {
    public Operator(String token, SymbolType type, ArrayList<Symbol> resultList, ArrayList<Symbol> pendingList) throws EvaluationException {
        super(token, type, resultList, pendingList);
        Log.v("com.mck.bin", "init has begun for " + token + " operator.");


    }

    protected Double toDouble(Symbol value) throws EvaluationException{
        try{
            return Double.valueOf(value.toString());
        } catch (NumberFormatException nfe){
            throw new EvaluationException("The value " + value + " can not be converted to a double.");
        }
    }
}

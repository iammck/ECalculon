package com.mck.ecalculon.com.mck.ecalculon.evaluator;

import android.util.Log;

import java.lang.*;
import java.util.ArrayList;

/**
 * Created by mike on 5/11/2015.
 */
public abstract class BinaryOperator extends Operator {
    public BinaryOperator(String token, SymbolType type, ArrayList<Symbol> result, ArrayList<Symbol> pending) throws EvaluationException {
        super(token, type, result, pending);

        // if this is the first operator in the pending list,
        if (pendingList.size() == 0){
            // add it and return.
            pendingList.add(this);
            return;
        }
        // while a last pendingList item exists and has a greater or equal
        // precedence over this operator.
        while (pendingList.size() > 0 &&
                pendingList.get(pendingList.size() - 1).compareTo(this) >= 0){
            // evaluate last pendingList.
            if (!pendingList.get(pendingList.size() - 1).evaluate()){
                // unable to evaluate the last operator, break out!
                break;
            }
        }

        // add this operator to pendingList.
        pendingList.add(this);

    }

    @Override
    public boolean evaluate() throws EvaluationException {
        Log.v("com.mck.bin", "evaluate() has begun for " + this.toString());

        // if there are not at least two Items in resultList,
        if( resultList.size() < 2){
            // add this back to pending and return false.
            return false;
        }

        // This item should be the last item in pendingList, remove it.
        pendingList.remove(this);

        // remove and grab the last resultList item and hold it as second number.
        Symbol second = resultList.remove(resultList.size() - 1);

        // while there is a pending list and
        // the last item has a greater or equal precedence over this operator.
        while ( pendingList.size() > 0 &&
                pendingList.get(pendingList.size() - 1).compareTo(this) >= 0){
            // evaluate last pendingList.
            if(!pendingList.get(pendingList.size() - 1).evaluate()){
                // There are still pending operators of greater precedence.
                // add stuff back to the lists then return false.
                resultList.add(second);
                pendingList.add(this);
                return false;
            }
        }

        // if there are not at least one more Item in resultList,
        if( resultList.size() < 1){
            // add stuff back to lists and return false.
            resultList.add(second);
            pendingList.add(this);
            return false;
        }

        // Remove and hold a second resultList Items as first number.
        Symbol first = resultList.remove(resultList.size() - 1);
        Log.v("com.mck.bin", this.toString() + " evaluate() will call operate with "
            + first + " as first and " + second + " as second.");
        operate(first, second);
        // operated, return true
        return true;
    }

    protected abstract void operate(Symbol first, Symbol second) throws EvaluationException;


}

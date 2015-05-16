package com.mck.ecalculon.com.mck.ecalculon.evaluator;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mike on 5/11/2015.
 */
public class Negation extends Operator {


    public Negation(ArrayList<Symbol> resultList, ArrayList<Symbol> pendingList) throws EvaluationException {
        super("-", SymbolType.negation, resultList, pendingList);

       /* // if this is the first operator in the pending list,
        if (pendingList.size() == 0){
            // add it and return.
            pendingList.add(this);
            return;
        }

        // while a last pendingList item exists and has a greater precedence
        // over this operator.
        while (pendingList.size() > 0 &&
                pendingList.get(pendingList.size() - 1).compareTo(this) > 0){
            // evaluate last pendingList.
            if (pendingList.get(pendingList.size() - 1).evaluate()){
                // unable to evaluate the last operator, break out!
                break;
            }
        }*/

        // add this operator to pendingList.
        pendingList.add(this);
    }

    @Override
    protected ArrayList<SymbolType> getEqualTo() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        result.add(SymbolType.negation);
        return result;
    }

    @Override
    protected ArrayList<SymbolType> getGreaterThen() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        result.add(SymbolType.multiplication);
        result.add(SymbolType.division);
        result.add(SymbolType.addition);
        result.add(SymbolType.subtraction);
        result.add(SymbolType.number);
        return result;
    }

    @Override
    public boolean evaluate() throws EvaluationException {
        Log.v("com.mck.bin", "evaluate() has begun for negation.");
        // if there is not at least one item in resultList,
        if( resultList.size() < 1){
            return false;
        }
        // This item should be the last item in pendingList, remove it.
        pendingList.remove(this);

        // Remove and grab the last resultList Item
        Symbol last = resultList.remove(resultList.size() - 1);
        Log.v("com.mck.bin", "negation evaluate() will call operate() with "
                + last + ".");
        // get the answer via negation of the first item.
        Double ans = - toDouble(last);

        // numbers add themselves to the resultList.
        NumberSymbol n = new NumberSymbol(
                ans.toString() , resultList, pendingList);

        // while there is a pending list and
        // the last item has a greater or equal precedence over this operator.
        while ( pendingList.size() > 0 &&
                pendingList.get(pendingList.size() - 1).compareTo(this) >= 0){
            // evaluate last pendingList.
            pendingList.get(pendingList.size() - 1).evaluate();
        }
        return true;
    }

}
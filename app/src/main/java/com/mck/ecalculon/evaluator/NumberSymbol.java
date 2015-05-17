package com.mck.ecalculon.evaluator;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mike on 5/11/2015.
 */
public class NumberSymbol extends Symbol{
    public NumberSymbol(String token, ArrayList<Symbol> result, ArrayList<Symbol> pending) throws EvaluationException {
        super(token, SymbolType.number, result, pending);
        Log.v("com.mck.exp", "NumberSymbol instantiated with token " + token);
        evaluate();

    }

    @Override
    protected ArrayList<SymbolType> getEqualTo() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        result.add(SymbolType.number);
        return result;
    }

    @Override
    protected ArrayList<SymbolType> getGreaterThen() {
        return new ArrayList<SymbolType>();
    }

    @Override
    public boolean evaluate() {
        // numbers just add them selves to the resultList as the last item.
        resultList.add(this);
        // should never be in pendingList, but what they hay.
        pendingList.remove(this) ;
        return true;
    }
}

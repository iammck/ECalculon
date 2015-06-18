package com.mck.ecalculon.evaluator;

import java.util.ArrayList;

/**
 * Created by mike on 6/14/2015.
 */
public class LeftParenthesis extends Symbol {

    public LeftParenthesis(ArrayList<Symbol> resultList, ArrayList<Symbol> pendingList)
            throws EvaluationException {

        super("(", SymbolType.lParenthesis, resultList, pendingList);
        pendingList.add(this);
    }

    @Override
    protected ArrayList<SymbolType> getEqualTo() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        return result;
    }

    @Override
    protected ArrayList<SymbolType> getGreaterThen() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        return result;
    }

    @Override
    public boolean evaluate() throws EvaluationException {
        return false;
    }
}

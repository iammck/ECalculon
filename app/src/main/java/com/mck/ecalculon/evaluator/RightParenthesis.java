package com.mck.ecalculon.evaluator;

import java.util.ArrayList;

/**
 * Created by mike on 6/14/2015.
 */
public class RightParenthesis extends Symbol {

    public RightParenthesis(ArrayList<Symbol> resultList, ArrayList<Symbol> pendingList)
            throws EvaluationException {
        super(")", SymbolType.rParenthesis, resultList, pendingList);
        if (!evaluate()){
            // if unable to evaluate, something is wrong.
            throw new EvaluationException("RightParenthesis could not finish evaluating.");
        }
    }

    @Override
    protected ArrayList<SymbolType> getEqualTo() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        result.add(SymbolType.rParenthesis);
        return result;
    }

    @Override
    protected ArrayList<SymbolType> getGreaterThen() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        return result;
    }

    @Override
    public boolean evaluate() throws EvaluationException {
        while (!pendingList.isEmpty()) {
            // get the last pending symbol.
            Symbol symbol = pendingList.get(pendingList.size() - 1);
            if (symbol.evaluate()) { // can it be evaluted?
                continue; // Otherwise, is it the matching left?
            } else if (symbol.type == SymbolType.lParenthesis) {
                pendingList.remove(symbol);
                return true;
            }
            throw new EvaluationException("RightParenthesis could not " +
                    "finish evaluating symbol " + symbol.toString());
        }
        // Did not return after removing a rParenthesis
        return false;
    }
}

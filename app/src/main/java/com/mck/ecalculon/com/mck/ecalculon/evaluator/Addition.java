package com.mck.ecalculon.com.mck.ecalculon.evaluator;

import com.mck.ecalculon.com.mck.ecalculon.evaluator.BinaryOperator;
import com.mck.ecalculon.com.mck.ecalculon.evaluator.NumberSymbol;
import com.mck.ecalculon.com.mck.ecalculon.evaluator.Symbol;

import java.util.ArrayList;

/**
 * Created by mike on 5/11/2015.
 */
public class Addition extends BinaryOperator {
    public Addition(ArrayList<Symbol> resultList, ArrayList<Symbol> pendingList) throws EvaluationException {
        super("+", SymbolType.addition, resultList, pendingList);
    }

    @Override
    protected void operate(Symbol first, Symbol second) throws EvaluationException {
        Double num1 = toDouble(first);
        Double num2 = toDouble(second);
        Double ans = num1 + num2;
        String result = String.valueOf(ans);
        // the number eval themselves to resultList.
        new NumberSymbol( result, resultList, pendingList);
    }

    @Override
    protected ArrayList<SymbolType> getEqualTo() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        result.add(SymbolType.addition);
        result.add(SymbolType.subtraction);
        return result;
    }

    @Override
    protected ArrayList<SymbolType> getGreaterThen() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        result.add(SymbolType.number);
        return result;
    }

}

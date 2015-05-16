package com.mck.ecalculon.com.mck.ecalculon.evaluator;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mike on 5/11/2015.
 */
public class Multiplication extends BinaryOperator {
    public Multiplication(ArrayList<Symbol> resultList, ArrayList<Symbol> pendingList) throws EvaluationException {
        super("x", SymbolType.multiplication, resultList, pendingList);
    }

    @Override
    protected void operate(Symbol first, Symbol second) throws EvaluationException {
        Double num1 = toDouble(first);
        Double num2 = toDouble(second);
        Double ans = num1 * num2;
        String token = String.valueOf(ans);
        // the number eval themselves to resultList.
        NumberSymbol n = new NumberSymbol(
                token, resultList, pendingList);
    }

    @Override
    protected ArrayList<SymbolType> getEqualTo() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        result.add(SymbolType.multiplication);
        result.add(SymbolType.division);
        return result;
    }

    @Override
    protected ArrayList<SymbolType> getGreaterThen() {
        ArrayList<SymbolType> result = new ArrayList<SymbolType>();
        result.add(SymbolType.addition);
        result.add(SymbolType.subtraction);
        result.add(SymbolType.number);
        return result;
    }
}

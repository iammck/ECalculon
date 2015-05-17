package com.mck.ecalculon.evaluator;

import java.util.ArrayList;

/**
 * Created by mike on 5/11/2015.
 */
public class Division extends BinaryOperator {
    public Division(ArrayList<Symbol> resultList, ArrayList<Symbol> pendingList) throws EvaluationException {
        super("/", SymbolType.division, resultList, pendingList);
    }

    @Override
    protected void operate(Symbol first, Symbol second) throws EvaluationException {
        Double num1 = toDouble(first);
        Double num2 = toDouble(second);
        // if the second number is a zero,
        if (num2.compareTo(Double.valueOf(0)) == 0){
            throw new EvaluationException("Divide by zero.");
        }
        Double ans = num1 / num2;
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

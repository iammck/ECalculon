package com.mck.ecalculon.evaluator;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mike on 5/11/2015.
 */
public abstract class Symbol implements Comparable<Symbol>{
    protected final String token;
    protected final SymbolType type;
    protected final ArrayList<Symbol> resultList;
    protected final ArrayList<Symbol> pendingList;

    // enum of all symbol types.
    public enum SymbolType {
        number, addition, subtraction,
        multiplication, division,
        negation, lParenthesis, rParenthesis
    }

    public Symbol(String token, SymbolType type, ArrayList<Symbol> resultList, ArrayList<Symbol> pendingList) throws EvaluationException{
        this.token = token;
        this.type = type;
        this.resultList = resultList;
        this.pendingList = pendingList;
    }

    /**
     *
     * @param another
     * @return a negative integer if this instance is less than another;
     *      a positive integer if this instance is greater than another;
     *      0 if this instance has the same order as another.
     */
    @Override
    public int compareTo(Symbol another) {
        Log.v("com.mck.binaryproblem", this.toString() + " compateTo() " + another.toString());
        if (getGreaterThen().contains(another.type)) return 1;
        if (getEqualTo().contains(another.type)) return 0;
        // not greater or equal, must be less than.
        return -1;
    }

    public String toString(){
        return token.toString();
    }

    protected abstract ArrayList<SymbolType> getEqualTo();

    protected abstract ArrayList<SymbolType> getGreaterThen();

    public abstract boolean evaluate() throws EvaluationException;
}

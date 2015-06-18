package com.mck.ecalculon.evaluator;

/**
 * Created by mike on 6/9/2015.
 */
public class InputButtons {

    /**
     * Can always click numbers.
     */
    public static boolean canClick(String currentExpression){
        return true;
    }
    public static class NumberButton {
    }

    /**TODO
     * can click the decimal button if the current number
     * does not contain a Decimal
     */
    public static class DecimalButton {
        public static boolean canClick(String currentExpression){
            return false;
        }
    }

    public static class MulitplyButton {
        public static boolean canClick(String currentExpression){
            return BinaryButton.canClick(currentExpression);
        }
    }

    public static class DivideButton {
        public static boolean canClick(String currentExpression){
            return BinaryButton.canClick(currentExpression);
        }
    }

    public static class AddButton {
        public static boolean canClick(String currentExpression){
            return BinaryButton.canClick(currentExpression);
        }
    }

    // TODO is this a unary or a binary
    public static class SubtractButton {
        public static boolean canClick(String currentExpression){
            return false;
        }
    }

    /**
     * TODO Can click if the last item is a number, decimal, or
     * parenthesis.
     */
    public static class BinaryButton {
        public static boolean canClick(String currentExpression){
            return false;
        }
    }

    //TODO can click if the previous is not a right parenthesis
    public static class UnaryButton {
        public static boolean canClick(String currentExpression){
            return false;
        }
    }

    //TODO is the left item a number?
    public static class LeftParenthesisButton {
        public static boolean canClick(String currentExpression){
            return false;
        }
    }

    // TODO is the left item a number
    public static class RightParenthesisButton {
        public static boolean canClick(String currentExpression){
            return false;
        }
    }

    // TODO if the current number does not have a decimal.
    public static class PiButton {
        public static boolean canClick(String currentExpression){
            return false;
        }
    }
}

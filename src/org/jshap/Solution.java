package org.jshap;

import org.jshap.containers.*;
import org.jshap.token.*;

public class Solution {
    /**
     * Перевод в постфиксную форму
     * @param equation арифметическое выражение
     * @param vars список объявленных переменных
     * @return список токенов
     */
    public static LinkedList<Token> toPostfixForm(final String equation, final LinkedList<ValueToken> vars) {
        LinkedList<Token> result = new LinkedList<>();
        LinkedList<Token> tokens = Lexer.getTokens(equation, vars);
        Stack<Token> operations = new Stack<>();

        for (int i = 0; i < tokens.size(); ++i) {
            switch(tokens.at(i).type()) {
                case TokenType.BINARY_OPERATION -> {
                    while (!operations.isEmpty() && getPriority(tokens.at(i)) <= getPriority(operations.top())) {
                        result.pushBack(operations.pop());
                    }
                    operations.push(tokens.at(i));
                }
                case TokenType.NUMBER,TokenType.VARIABLE,TokenType.FUNCTION -> result.pushBack(tokens.at(i));
                case TokenType.BRACE -> {
                    switch (((Brace) tokens.at(i)).brace()) {
                        case BraceType.OPEN_BRACKET -> operations.push(tokens.at(i));
                        case BraceType.CLOSE_BRACKET -> {
                            while (operations.top().type() != TokenType.BRACE) {
                                result.pushBack(operations.pop());
                            }
                            operations.pop();
                        }
                    }
                }
            }
        }
        while (!operations.isEmpty()) {
            result.pushBack(operations.pop());
        }
        return result;
    }

    /**
     * Получение приоритета бинарных операций
     * @param token токен
     * @return приоритет
     */
    private static byte getPriority(Token token) {
        if (token instanceof Binary) {
            return switch (((Binary) token).operation()) {
                case BinaryType.PLUS, BinaryType.MINUS -> 1;
                case BinaryType.MULTIPLY, BinaryType.DIVIDE, BinaryType.POWER -> 2;
            };
        } else {
            return 0;
        }
    }

    /**
     * Подсчет арифметического выражения
     * @param equation арифметическое выражения
     * @param vars список объявленных переменных
     * @return результат выражения
     */
    public static Double calcEquation(final String equation, LinkedList<ValueToken> vars) {
        LinkedList<Token> postfixForm = toPostfixForm(equation, vars);
        Stack<Double> values = new Stack<>();

        for (int i = 0; i < postfixForm.size(); ++i) {
            if (postfixForm.at(i) instanceof DataToken) {
                values.push(((DataToken) postfixForm.at(i)).value());
            } else if (postfixForm.at(i) instanceof Binary) {
                Double right = values.pop();
                Double left = values.pop();

                switch (((Binary) postfixForm.at(i)).operation()) {
                    case BinaryType.PLUS -> values.push(left + right);
                    case BinaryType.MINUS -> values.push(left - right);
                    case BinaryType.MULTIPLY -> values.push(left * right);
                    case BinaryType.DIVIDE -> values.push(left / right);
                    case BinaryType.POWER -> values.push(Math.pow(left, right));
                }
            } else if (postfixForm.at(i) instanceof ValueToken) {
                Double value = ((ValueToken) postfixForm.at(i)).value();

                if (((ValueToken) postfixForm.at(i)).isInverted()) {
                    value *= -1;
                }

                values.push(value);
            } else if (postfixForm.at(i) instanceof Function) {
                Double param = calcEquation(((Function) postfixForm.at(i)).param(), vars);
                Double value = 1.;

                switch(((Function) postfixForm.at(i)).function()) {
                    case FunctionType.SIN -> value = Math.sin(param);
                    case FunctionType.COS -> value = Math.cos(param);
                    case FunctionType.TAN -> value = Math.tan(param);
                    case FunctionType.EXP -> value = Math.exp(param);
                }

                if (((Function) postfixForm.at(i)).isInverted()) {
                    value *= -1;
                }

                values.push(value);
            }
        }

        return values.top();
    }
    /**
     * Проверка правильности введённого выражения
     * @param equation арифметическое выражение
     * @return булевое значение
     */
    public static boolean isProperlyArranged(final String equation, final LinkedList<Token> tokens) {
        Stack<Character> stack = new Stack<>();

        for (Character ch : equation.toCharArray()) {
            switch(ch) {
                case'(' -> stack.push(')');
                case')' -> {
                    if (stack.isEmpty() || stack.top() != ch) {
                        return false;
                    } else {
                        stack.pop();
                    }
                }
            }
        }

        if (!stack.isEmpty()) {
            return false;
        }

        for (int i = 0; i < tokens.size(); ++i) {
            switch (tokens.at(i).type()) {
                case TokenType.NUMBER,TokenType.VARIABLE,TokenType.FUNCTION -> {
                    // проверка, когда число стоит перед открытой, после закрытой скобки
                    if (i > 0 && tokens.at(i - 1).type() == TokenType.BRACE &&
                            ((Brace) tokens.at(i - 1)).brace() == BraceType.CLOSE_BRACKET) {
                        return false;
                    }

                    if (i < tokens.size() - 1 && tokens.at(i + 1).type() == TokenType.BRACE &&
                            ((Brace) tokens.at(i + 1)).brace() == BraceType.OPEN_BRACKET) {
                        return false;
                    }

                    // проверка на отсутствие операции между числами
                    if (tokens.size() > 1 && i > 0) {
                        switch (tokens.at(i - 1).type()) {
                            case TokenType.NUMBER,TokenType.VARIABLE,TokenType.FUNCTION -> {
                                return false;
                            }
                        }

                        if (i == tokens.size() - 1) {
                            continue;
                        }

                        switch (tokens.at(i + 1).type()) {
                            case TokenType.NUMBER,TokenType.VARIABLE,TokenType.FUNCTION -> {
                                return false;
                            }
                        }
                    }
                }
                case TokenType.BINARY_OPERATION -> {
                    if (i == 0 || i == tokens.size() - 1) {
                        // находится ли операции скраю
                        return false;
                    }

                    if (tokens.at(i - 1).type() == TokenType.BINARY_OPERATION ||
                            tokens.at(i + 1).type() == TokenType.BINARY_OPERATION ||
                            tokens.at(i + 1).type() == TokenType.BRACE &&
                                    ((Brace) tokens.at(i + 1)).brace() == BraceType.CLOSE_BRACKET) {
                        // две операции, но нет чисел между ними, и операция перед закрывающейся скобкой
                        return false;
                    }
                }
                case TokenType.BRACE -> { }
            }
        }

        return true;
    }
}
package org.jshap.token;

import java.util.StringTokenizer;
import org.jshap.containers.LinkedList;

/**
 * Класс для работы со строкой
 */
public class Lexer {
    /**
     * Разделители строки
     */
    private static final String DELIMITERS = " ";

    /**
     * Получение списка токенов
     * @param equation арифметическое выражение
     * @param vars список переменных
     * @return список токенов
     */
    public static LinkedList<Token> getTokens(final String equation, final LinkedList<ValueToken> vars) {
        LinkedList<Token> tokens = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(equation, DELIMITERS, true);

        while (tokenizer.hasMoreTokens()) {
            String curToken = tokenizer.nextToken();

            if (curToken.isBlank()) {
                continue;
            }

            if (isNumber(curToken)) { // Пуш чисел
                tokens.pushBack(new DataToken(Double.parseDouble(curToken)));
            } else if (findVar(curToken, vars) != -1) { // Пуш переменных
                int ind = findVar(curToken, vars);

                if (curToken.charAt(0) == '-') {
                    tokens.pushBack(new ValueToken(vars.at(ind).name(), vars.at(ind).value(), true));
                } else {
                    tokens.pushBack(vars.at(ind));
                }
            } else if (curToken.length() > 3 && curToken.contains("(") && isFun(curToken.substring(0, curToken.indexOf('(')))) { //Пуш функций
                String funName = curToken.substring(0, curToken.indexOf('('));
                boolean isInverted = false;

                if ('+' == curToken.charAt(0) || '-' == curToken.charAt(0)) {
                    funName = funName.substring(1);
                    isInverted = true;
                }

                String param = getFunParam(curToken, tokenizer);

                tokens.pushBack(makeToken(funName, param, isInverted));
            } else { // Пуш скобок и бинарных операций
                tokens.pushBack(makeToken(curToken, "", false));
            }
        }

        return tokens;
    }

    /**
     * Проверка является ли заданная строка числом
     * @param string строка
     * @return булевое значение
     */
    public static boolean isNumber(final String string) {
        String str = string;

        if ('+' == str.charAt(0) || '-' == str.charAt(0)) {
            if (str.length() == 1) {
                return false;
            }

            str = string.substring(1);
        }

        if ('.' == str.charAt(0)) {
            return false;
        }

        int amountOfPoints = 0;

        for (int i = 0; i < str.length(); ++i) {
            if (!Character.isDigit(str.charAt(i)) && '.' != str.charAt(i)) {
                return false;
            }

            if ('.' == str.charAt(i)) {
                ++amountOfPoints;
            }
        }

        return amountOfPoints <= 1;
    }

    /**
     * Поиск заданной в выражении переменной в списке объявленных переменных
     * @param varName переменная, которая ищется в списке
     * @param vars список переменных
     * @return позиция переменной в списке, в случае необнаружения возвращает -1
     */
    public static int findVar(final String varName, final LinkedList<ValueToken> vars) {
        String var = varName;

        if ('+' == varName.charAt(0) || '-' == varName.charAt(0)) {
            if (varName.length() == 1) {
                return -1;
            }

            var = varName.substring(1);
        }

        for (int i = 0; i < vars.size(); ++i) {
            if (vars.at(i).name().equals(var)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Поиск функции в качестве реализованной
     * @param funName функция
     * @return булевое значение
     */
    public static boolean isFun(final String funName) {
        String fun = funName;

        if ('+' == funName.charAt(0) || '-' == funName.charAt(0)) {
            fun = funName.substring(1);
        }

        switch(fun) {
            case"sin","cos","tan","atan","log","log10",
                    "abs","exp" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Вычленение параметра из функции
     * @param curToken текущий токен
     * @param tokenizer токенайзер
     * @return параметр функции
     */
    public static String getFunParam(String curToken, final StringTokenizer tokenizer) {
        String param = "";
        int braceDifference = 0;

        curToken = curToken.substring(curToken.indexOf('('));
        do {
            for (int i = 0; i < curToken.length(); ++i) {
                if (curToken.charAt(i) == '(') {
                    ++braceDifference;
                } else if (curToken.charAt(i) == ')') {
                    --braceDifference;
                }
            }

            param += curToken;
            if (tokenizer.hasMoreTokens()) {
                curToken = tokenizer.nextToken();
            }
        } while (braceDifference != 0);

        param = param.substring(1, param.length() - 1);

        return param;
    }

    /**
     * Генерации Function и Binary токенов
     * @param token токен
     * @param param параметр функции
     * @param isInverted есть ли минус
     * @return токен
     */
    private static Token makeToken(final String token, final String param, final boolean isInverted) {
        switch (token) {
            case"sin" -> {
                return new Function(FunctionType.SIN, param, isInverted);
            }
            case"cos" -> {
                return new Function(FunctionType.COS, param, isInverted);
            }
            case"tan" -> {
                return new Function(FunctionType.TAN, param, isInverted);
            }
            case"exp" -> {
                return new Function(FunctionType.EXP, param, isInverted);
            }
            case"+" -> {
                return new Binary(BinaryType.PLUS);
            }
            case"-" -> {
                return new Binary(BinaryType.MINUS);
            }
            case"*" -> {
                return new Binary(BinaryType.MULTIPLY);
            }
            case"/" -> {
                return new Binary(BinaryType.DIVIDE);
            }
            case"^" -> {
                return new Binary(BinaryType.POWER);
            }
            case"(" -> {
                return new Brace(BraceType.OPEN_BRACKET);
            }
            case")" -> {
                return new Brace(BraceType.CLOSE_BRACKET);
            }
            default -> throw new RuntimeException("Unexpected token " + token);
        }
    }
}
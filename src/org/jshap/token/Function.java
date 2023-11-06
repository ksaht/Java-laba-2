package org.jshap.token;

/**
 * Функция
 * @param function тип функции
 * @param param параметр функции
 * @param isInverted есть ли минус
 */
public record Function(
        FunctionType function,
        String param,
        boolean isInverted
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.FUNCTION;
    }
}
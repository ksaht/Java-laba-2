package org.jshap.token;

/**
 * Объявленная переменная
 * @param name имя переменной
 * @param value значение переменной
 * @param isInverted есть ли минус
 */
public record ValueToken (
        String name,
        Double value,
        boolean isInverted
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.VARIABLE;
    }
}
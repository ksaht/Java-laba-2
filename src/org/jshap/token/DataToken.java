package org.jshap.token;

/**
 * Значение
 * @param value хранимое число
 */
public record DataToken (
        Double value
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.NUMBER;
    }
}
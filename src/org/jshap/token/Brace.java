package org.jshap.token;

/**
 * Скобка
 * @param brace тип скобки
 */
public record Brace (
        BraceType brace
) implements Token {
    public TokenType type() {
        return TokenType.BRACE;
    }
}
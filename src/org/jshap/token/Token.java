package org.jshap.token;

/**
 * Интерфейс для токенов
 */
public interface Token {
    /**
     * Определение типа токена
     * @return TokenType тип токена
     */
    public TokenType type();
}
package org.jshap.containers;

/**
 * Реализация стека
 * @param <T> тип данных
 */
public class Stack <T> {
    /**
     * Node класс узлов
     */
    final private static class Node <T> {
        final private T data;
        private Node<T> next;

        /**
         * Конструктор с параметром
         * @param data данные
         */
        private Node(T data) {
            this.data = data;
        }

        /**
         * Переопределенный метод toString
         * @return возвращает строку с информацией
         */
        @Override
        public String toString() {
            return data.toString();
        }
    }

    private Node<T> head;
    private int size;

    /**
     * Конструктор по умолчанию
     */
    public Stack() {
        head = null;
        size = 0;
    }

    /**
     * Добавление элемента в стек
     * @param data данные
     */
    public void push(T data) {
        if (head == null) {
            head = new Node<>(data);
        } else {
            Node<T> newHead = new Node<>(data);
            newHead.next = head;
            head = newHead;
        }

        ++size;
    }

    /**
     * Получение значения (которое хранится наверху)
     * @return значение
     */
    public T top() {
        return head.data;
    }

    /**
     * Удаление элемента (которое хранится наверху)
     * @return значение (которое удалили)

     */
    public T pop() {
        T returnVal = head.data;
        head = head.next;

        --size;

        return returnVal;
    }

    /**
     *Очистка стека
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Проверка на пустоту
     * @return булевое значение
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Получения размерности стека
     * @return размер стека
     */
    public int size() {
        return size;
    }

    /**
     * Переопределенный метод toString
     * @return возвращает строку с информацией об элементах
     */
    @Override
    public String toString() {
        String str = "[ ";

        Stack<T> newStack = new Stack<>();
        while (head != null) {
            str += "\n\t";
            newStack.push(this.pop());
            str += newStack.head.toString();
        }

        while (newStack.head != null) {
            this.push(newStack.pop());
        }

        str += "\n ]";

        return str;
    }
}
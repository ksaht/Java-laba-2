package org.jshap;

import java.util.Scanner;
import org.jshap.containers.LinkedList;
import org.jshap.token.ValueToken;
import static org.jshap.Solution.calcEquation;

public class Main {
    /**
     * Ввод переменных с клавиатуры
     * @return список из переменных
     */
    public static LinkedList<ValueToken> setVars() {
        LinkedList<ValueToken> vars = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String name = scanner.next();
            if(".".equals(name)) {
                return vars;
            }
            String value = scanner.next();

            vars.pushBack(new ValueToken(name, Double.parseDouble(value), false));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Арифметическое выражение: ");
        String equation = scanner.nextLine();
        // e.g 1  "cos(x) - 1"
        // e.g 2  "x ^ ( 2 + 1 ) + 5 * x + 45"
        // e.g 3  "x + y + z"
        System.out.println("Значения переменных формат: name value (в конце точка): ");
        LinkedList<ValueToken> vars = setVars();
        // e.g 1  "x 4 ."
        // e.g 2  "x 5 ."
        // e.g 3  "x 6 y 7 z 8 ."
        System.out.printf("Ответ: " + calcEquation(equation, vars).toString());
        // 1 ~-1.6
        // 2 195
        //3 21
    }
}
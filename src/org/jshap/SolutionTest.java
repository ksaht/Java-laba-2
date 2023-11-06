package org.jshap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jshap.containers.LinkedList;
import org.jshap.token.ValueToken;

class SolutionTest {
    /**
     * Проверка подсчёта арифметических выражений
     */
    @Test
    void calcExpressionTest() {
        Double value1 = Math.exp(4) + 3. / 14;
        String expression1 = "exp(2 * y) + 3. / 14";
        LinkedList<ValueToken> vars1 = new LinkedList<>();
        vars1.pushBack(new ValueToken("y", 2., false));

        Double value2 = Math.cos(4) + 2;
        String expression2 = "cos(2 * y) + 2";
        LinkedList<ValueToken> vars2 = new LinkedList<>();
        vars2.pushBack(new ValueToken("y", 2., false));

        Double value3 = Math.sin(4) + 2;
        String expression3 = "sin(2 * y) + 2";
        LinkedList<ValueToken> vars3 = new LinkedList<>();
        vars3.pushBack(new ValueToken("y", 2., false));

        Double value4 = Math.tan(4) + 2;
        String expression4 = "tan(2 * y) + 2";
        LinkedList<ValueToken> vars4 = new LinkedList<>();
        vars4.pushBack(new ValueToken("y", 2., false));

        int value5 = 153*356 ;
        String expression5 = "153 * y";
        LinkedList<ValueToken> vars5 = new LinkedList<>();
        vars5.pushBack(new ValueToken("y", 356., false));

        int value6 = 356 - 100 ;
        String expression6 = "y - 100";
        LinkedList<ValueToken> vars6 = new LinkedList<>();
        vars6.pushBack(new ValueToken("y", 356., false));

        int value7 = 355 / 5;
        String expression7 = "y / 5";
        LinkedList<ValueToken> vars7 = new LinkedList<>();
        vars7.pushBack(new ValueToken("y", 355., false));

        assertEquals(value1, Solution.calcEquation(expression1, vars1));
        assertEquals(value2, Solution.calcEquation(expression2, vars2));
        assertEquals(value3, Solution.calcEquation(expression3, vars3));
        assertEquals(value4, Solution.calcEquation(expression4, vars4));
        assertEquals(value5, Solution.calcEquation(expression5, vars5));
        assertEquals(value6, Solution.calcEquation(expression6, vars6));
        assertEquals(value7, Solution.calcEquation(expression7, vars7));
    }
}
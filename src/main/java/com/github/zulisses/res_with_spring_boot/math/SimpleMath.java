package com.github.zulisses.res_with_spring_boot.math;

public class SimpleMath {
    public double sum(Double numberOne, Double numberTwo) {
        return numberOne + numberTwo;
    }

    public double subtraction(Double numberOne, Double numberTwo) {
        return numberOne - numberTwo;
    }

    public double multiplication(Double numberOne, Double numberTwo) {
        return numberOne * numberTwo;
    }

    public double division(Double numberOne, Double numberTwo) {
        return numberOne / numberTwo;
    }

    public double mean(Double numberOne, Double numberTwo) {
        return (numberOne + numberTwo)/2;
    }

    public double squareRoot(Double numberOne) {
        return Math.sqrt(numberOne);
    }
}

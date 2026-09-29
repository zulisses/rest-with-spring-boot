package com.github.zulisses.res_with_spring_boot.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.github.zulisses.res_with_spring_boot.exception.UnsupportedMathOperationException;
import com.github.zulisses.res_with_spring_boot.request.converters.NumberConverter;
import com.github.zulisses.res_with_spring_boot.math.SimpleMath;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/math")
public class MathController {

    private SimpleMath simpleMath = new SimpleMath();

    // http://localhost:8080/math/sum/3/5
    @RequestMapping("/sum/{numberOne}/{numberTwo}")
    public double sum(@PathVariable String numberOne, @PathVariable String numberTwo) {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return simpleMath.sum(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    // http://localhost:8080/math/subtraction/2/3
    @RequestMapping("/subtraction/{numberOne}/{numberTwo}")
    public double subtraction(@PathVariable String numberOne, @PathVariable String numberTwo) {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return simpleMath.subtraction(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    // http://localhost:8080/math/multiplication/2/3
    @RequestMapping("/multiplication/{numberOne}/{numberTwo}")
    public double multiplication(@PathVariable String numberOne, @PathVariable String numberTwo) {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return simpleMath.multiplication(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    // http://localhost:8080/math/division/2/3
    @RequestMapping("/division/{numberOne}/{numberTwo}")
    public double division(@PathVariable String numberOne, @PathVariable String numberTwo) {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return simpleMath.division(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    // http://localhost:8080/math/mean/2/3
    @RequestMapping("/mean/{numberOne}/{numberTwo}")
    public double mean(@PathVariable String numberOne, @PathVariable String numberTwo) {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return simpleMath.mean(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    // http://localhost:8080/math/squareroot/2
    @RequestMapping("/squareroot/{numberOne}")
    public double squareRoot(@PathVariable String numberOne) {
        if(!NumberConverter.isNumeric(numberOne)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return simpleMath.squareRoot(NumberConverter.convertToDouble(numberOne));
    }

}

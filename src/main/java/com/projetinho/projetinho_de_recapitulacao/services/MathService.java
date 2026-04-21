package com.projetinho.projetinho_de_recapitulacao.services;

import com.projetinho.projetinho_de_recapitulacao.exception.UnsupportedMathOperationException;
import org.springframework.stereotype.Service;

@Service
public class MathService {

    public Double sum(String numberOne, String numberTwo) {
        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    public Double subtraction(String numberOne, String numberTwo) {
        return convertToDouble(numberOne) - convertToDouble(numberTwo);
    }

    public Double multiplication(String numberOne, String numberTwo) {
        return convertToDouble(numberOne) * convertToDouble(numberTwo);
    }

    public Double division(String numberOne, String numberTwo) {
        Double divisor = convertToDouble(numberTwo);

        if (divisor == 0) {
            throw new UnsupportedMathOperationException("Division by zero is not allowed.");
        }

        return convertToDouble(numberOne) / divisor;
    }

    public Double mean(String numberOne, String numberTwo) {
        return sum(numberOne, numberTwo) / 2;
    }

    public Double squareRoot(String number) {
        Double convertedNumber = convertToDouble(number);

        if (convertedNumber < 0) {
            throw new UnsupportedMathOperationException("Square root of negative number is not allowed.");
        }

        return Math.sqrt(convertedNumber);
    }

    private Double convertToDouble(String strNumber) {
        if (!isNumeric(strNumber)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value.");
        }

        String number = strNumber.replace(",", ".");
        return Double.parseDouble(number);
    }

    private boolean isNumeric(String strNumber) {
        if (strNumber == null || strNumber.isEmpty()) {
            return false;
        }

        String number = strNumber.replace(",", ".");
        return number.matches("[+-]?[0-9]*\\.?[0-9]+");
    }
}

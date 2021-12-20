package com.example.homework.kotlin.calculator.calculatorExpression

import com.example.kotlin.course.calculator.Calculator
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

private const val BRACKET = "("
private const val BACK_BRACKET = ")"

class CalculateExpression {
    /*
    * Determines if there are brackets in an expression
    */
     private fun String.containsBrackets(): Boolean {
        return this.contains(BRACKET) && this.contains(BACK_BRACKET)
    }

    /*
     * A function that determines how to parse an expression, with or without brackets
     */
    fun calculateExpressionWithBrackets(expression: String): String {
        // remove all spaces
        val expressionWithoutSpaces = expression.replace(Regex("\\s+"), "")
        if (!expressionWithoutSpaces.containsBrackets()) return parseExpression(expressionWithoutSpaces)

        var result = expressionWithoutSpaces
        while (result.containsBrackets()) {
            result = parseBracketsExpression(result)
        }
        return parseExpression(result)
    }

    /*
     * Parse expression with brackets
     */
    private fun parseBracketsExpression(expression: String): String {
        val bracketIndex = expression.indexOf(BRACKET)
        val backBracketIndex = expression.indexOf(BACK_BRACKET)
        val simpleExpressionWithBrackets = expression.substring(bracketIndex, backBracketIndex + 1)
        val expressionWithoutBrackets = simpleExpressionWithBrackets
            .replace(BRACKET, "")
            .replace(BACK_BRACKET, "")
        val simpleExpressionResult = parseExpression(expressionWithoutBrackets)
        return expression.replace(simpleExpressionWithBrackets, simpleExpressionResult)
    }

    private fun isNumber(str: String): Boolean {
        return try {
            str.toBigDecimal()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

/*
* MAIN PARSE FUNCTION
*
* Parse expression without brackets
*  */

    private fun parseExpression(expression: String): String {
        if (isNumber(expression)) return expression
        var result = expression
        while (!isNumber(result)) {
            result = parse(result)
        }
        return result
    }

    private fun parse(expression: String): String {
        val calculator = Calculator()
        var operator = ""
        for (i in 0..calculator.operators.size) {
            operator = calculator.operators[i]
            if (expression.contains(operator)) {
                break
            }
        }
        return calculateExpressionByOperator(expression, operator)
    }

    private fun calculateExpressionByOperator(expression: String, operator: String): String {
        val operatorIndex = expression.indexOf(operator)
        val leftNumberIndex = expression.leftSymbolIndex(operatorIndex)
        val rightNumberIndex = expression.rightSymbolIndex(operatorIndex)
        val simpleExpression = expression.substring(leftNumberIndex, rightNumberIndex + 1)
        val simpleExpressionResult = calculate(simpleExpression, operator)
        return expression.replace(simpleExpression, simpleExpressionResult.toString())
    }

    /*
     * Search character to the left of the operator
     */
    private fun String.leftSymbolIndex(operatorIndex: Int): Int {
        var indexedValue = operatorIndex
        for (i in (operatorIndex - 1) downTo 0) {
            if (i == 0) indexedValue = 0
            val value = this[i].toString() // number, operator, start
            if (value.isOperator()) {
                indexedValue = i + 1
                break
            }
        }
        return indexedValue
    }

    /*
     * Search character to the right of the operator
     */
    private fun String.rightSymbolIndex(operatorIndex: Int): Int {
        val expressionLastIndex = this.length - 1
        var indexedValue = 0
        for (i in operatorIndex + 1..expressionLastIndex) {
            if (i == expressionLastIndex) indexedValue = expressionLastIndex
            val value = this[i].toString() // number, operator
            if (value.isOperator()) {
                indexedValue = i - 1
                break
            }
        }
        return indexedValue
    }

    private fun String.isOperator(): Boolean {
        val calculator = Calculator()
        return calculator.operators.contains(this)
    }

    private fun calculate(expression: String, operator: String): BigDecimal {
        val numbers = expression.split(operator)
        val value1: BigDecimal
        val value2: BigDecimal
        try {
            value1 = numbers[0].toBigDecimal()
            value2 = numbers[1].toBigDecimal()
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Input Error")
        } catch (e: IndexOutOfBoundsException) {
            throw NumberFormatException("Input Error")
        }
        // Command pattern
        val calculator = Calculator()
        return calculator.executeCommand(operator, value1, value2)!!
    }
}
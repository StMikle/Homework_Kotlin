package com.example.kotlin.course.calculator.calculatorExpression

import com.example.kotlin.course.calculator.api.Command
import com.example.kotlin.course.calculator.Calculator
import java.lang.NumberFormatException
import java.math.BigDecimal

const val BRACKET = "("
const val BACK_BRACKET = ")"

/*
 * Determines if there are brackets in an expression
 */
fun containsBrackets(string: String): Boolean {
    return string.contains(BRACKET) && string.contains(BACK_BRACKET)
}

/*
 * A function that determines how to parse an expression, with or without brackets
 */
fun calculateExpressionWithBrackets(expression: String): String {
    // remove all spaces
    val expressionWithoutSpaces = expression.replace(Regex("\\s+"), "")
    if (!containsBrackets(expressionWithoutSpaces)) return parseExpression(expressionWithoutSpaces)

    var result = expressionWithoutSpaces
    while (containsBrackets(result)) {
        result = parseBracketsExpression(result)
    }
    return parseExpression(result)
}

/*
 * Parse expression with brackets
 */
fun parseBracketsExpression(expression: String): String {
    val bracketIndex = expression.indexOf(BRACKET)
    val backBracketIndex = expression.indexOf(BACK_BRACKET)
    val simpleExpressionWithBrackets = expression.substring(bracketIndex, backBracketIndex + 1)
    val expressionWithoutBrackets = simpleExpressionWithBrackets
        .replace(BRACKET, "")
        .replace(BACK_BRACKET, "")
    val simpleExpressionResult = parseExpression(expressionWithoutBrackets)
    return expression.replace(simpleExpressionWithBrackets, simpleExpressionResult)
}

fun isNumber(str: String): Boolean {
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

fun parseExpression(expression: String): String {
    if (isNumber(expression)) return expression
    var result = expression
    while (!isNumber(result)) {
        result = parse(result)
    }
    return result
}

fun parse(expression: String): String {
    val calculator = Calculator()
    for (i in 0..calculator.commands.size) {
        val operator = calculator.operators[i]
        if (expression.contains(operator)) {
            return calculateExpressionByOperator(expression, operator)
        }
    }
    throw Error("operator not found")
}

fun calculateExpressionByOperator(expression: String, operator: String): String {
    val operatorIndex = expression.indexOf(operator)
    val leftNumberIndex = leftSymbolIndex(expression, operatorIndex)
    val rightNumberIndex = rightSymbolIndex(expression, operatorIndex)
    val simpleExpression = expression.substring(leftNumberIndex, rightNumberIndex + 1)
    val simpleExpressionResult = calculate(simpleExpression, operator)
    return expression.replace(simpleExpression, simpleExpressionResult.toString())
}

/*
 * Search character to the left of the operator
 */
fun leftSymbolIndex(expression: String, operatorIndex: Int): Int {
    var indexedValue = operatorIndex
    for (i in (operatorIndex - 1) downTo 0) {
        if (i == 0) indexedValue = 0
        val value = expression[i].toString() // number, operator, start
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
fun rightSymbolIndex(expression: String, operatorIndex: Int): Int {
    val expressionLastIndex = expression.length - 1
    var indexedValue = 0
    for (i in operatorIndex + 1..expressionLastIndex) {
        if (i == expressionLastIndex) indexedValue = expressionLastIndex
        val value = expression[i].toString() // number, operator
        if (value.isOperator()) {
            indexedValue = i - 1
            break
        }
    }
    return indexedValue
}

fun String.isOperator() : Boolean {
    val calculator = Calculator()
    return calculator.operators.contains(this)
}

fun calculate(expression: String, operator: String): BigDecimal {
    val numbers = expression.split(operator)
    val value1: BigDecimal
    val value2: BigDecimal
    try {
        value1 = numbers[0].toBigDecimal()
        value2 = numbers[1].toBigDecimal()
    } catch (e: NumberFormatException) {
        throw NumberFormatException("Input Error")
    }

    // Command pattern
    val calculator = Calculator()
    val commands = calculator.commands
    val command: Command? = commands[operator]
    return calculator.executeCommand(command, value1, value2)!!
}
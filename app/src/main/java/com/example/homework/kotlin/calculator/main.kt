package com.example.kotlin.course.calculator

import com.example.kotlin.course.calculator.api.Command
import com.example.kotlin.course.calculator.calculatorExpression.calculateExpressionWithBrackets
import java.lang.NumberFormatException
import java.math.BigDecimal

fun main() {
    val calculator = Calculator()
    val condition = "Enter the version of the calculator:\n" +
            "1) - Serial input of numbers from the keyboard\n" +
            """2) - Parsing expressions without \ with brackets"""
    println(condition)
    when(readLine()) {
        "1" -> runSerialCalculator(calculator)
        "2" -> runExpressionCalculator()
    }

}

fun runExpressionCalculator() {
    println("Example: 5 + 5 / 3 + 7 * 9 or (5 + 5) / 3 + 7 * 9")
    var result: String
    while (true) {
        println("Enter expression, 'end' to exit")
        when (val initString = readLine()?.replace(Regex("\\s+"), "")?.trim()) {
            "end" -> break
            else -> {
                try {
                    result = calculateExpressionWithBrackets(initString!!)
                } catch (e: NumberFormatException) {
                    println("Input Error")
                    continue
                }
                println("result: $result")
            }
        }
    }
}

fun runSerialCalculator(calculator: Calculator) {
    println("Example in console:\n" +
            "   Input: 2+2\n" +
            "   Output:4\n" +
            "   Input:4(from previous result) *2\n" +
            "   Output:8\n" +
            "   Input:8(from previous result) / 3\n")
    val commands = calculator.commands
    println("Enter 'end' to exit")
    var result = BigDecimal("0")
    loop@ while (true) {
        when (val initString = readLine()?.replace(Regex("\\s+"), "")?.trim()) {
            "end" -> break@loop
            else -> {
                val sign = calculator.commands.keys.firstOrNull { initString?.contains(it)!! }
                val split = when (sign) {
                    null -> {
                        println("Operator not found!!!")
                        continue@loop
                    }
                    else -> initString?.split(sign)
                }
                val value1 = when (split?.get(0)) {
                    "" -> result
                    else -> {
                        try {
                            split?.get(0).toString().toBigDecimal()
                        } catch (e: NumberFormatException) {
                            println("Not a number!!!")
                            continue@loop
                        }
                    }
                }
                val value2 = try {
                    split?.get(1).toString().toBigDecimal()
                } catch (e: NumberFormatException) {
                    println("Not a number!!!")
                    continue@loop
                }
                val command: Command? = commands[sign]
                result = calculator.executeCommand(command, value1, value2)!!
                print("$result ")
            }
        }
    }
}


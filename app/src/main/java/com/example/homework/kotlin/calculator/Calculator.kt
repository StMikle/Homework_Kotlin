package com.example.kotlin.course.calculator

import com.example.kotlin.course.calculator.api.Command
import com.example.kotlin.course.calculator.mathCommand.DivideCommand
import com.example.kotlin.course.calculator.mathCommand.MinusCommand
import com.example.kotlin.course.calculator.mathCommand.PlusCommand
import com.example.kotlin.course.calculator.mathCommand.TimesCommand
import java.math.BigDecimal

class Calculator {
    private val commands = mutableMapOf<String, Command?>(
        "*" to TimesCommand(), "/" to DivideCommand(),
        "+" to PlusCommand(), "-" to MinusCommand()
    )

    val operators: List<String>
        get() = commands.map { it.key }

    fun addCommand(operator: String, command: Command?) {
        commands[operator] = command
    }

    private fun findOperator(operator: String) : Command? {
        return commands[operator]
    }

    fun executeCommand(operator: String, value1: BigDecimal, value2: BigDecimal): BigDecimal? {
        return findOperator(operator)?.execute(value1, value2)
    }
}
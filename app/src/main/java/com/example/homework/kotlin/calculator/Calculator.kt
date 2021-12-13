package com.example.kotlin.course.calculator

import com.example.kotlin.course.calculator.api.Command
import com.example.kotlin.course.calculator.mathCommand.DivideCommand
import com.example.kotlin.course.calculator.mathCommand.MinusCommand
import com.example.kotlin.course.calculator.mathCommand.PlusCommand
import com.example.kotlin.course.calculator.mathCommand.TimesCommand
import java.math.BigDecimal

class Calculator {
    val commands = mutableMapOf<String, Command?>("*" to TimesCommand(), "/" to DivideCommand(),
        "+" to PlusCommand(), "-" to MinusCommand())

    val operators = commands.map{ it.key }

    fun addCommand(sign: String, command: Command?) {
        commands[sign] = command
    }

    fun executeCommand(command: Command?, value1: BigDecimal, value2: BigDecimal): BigDecimal? {
        return command?.execute(value1, value2)
    }
}
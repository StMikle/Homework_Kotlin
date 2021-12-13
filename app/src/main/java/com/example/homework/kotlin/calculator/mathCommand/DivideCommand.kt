package com.example.kotlin.course.calculator.mathCommand

import com.example.kotlin.course.calculator.api.Command
import java.lang.ArithmeticException
import java.math.BigDecimal
import java.math.RoundingMode

class DivideCommand: Command {
    override fun execute(value1: BigDecimal, value2: BigDecimal): BigDecimal {
        return try {
            value1.divide(value2, 4, RoundingMode.HALF_UP)
        } catch (e: ArithmeticException) {
            println("Division by zero !!!")
            value1
        }
    }
}
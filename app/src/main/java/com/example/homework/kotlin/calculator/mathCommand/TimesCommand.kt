package com.example.kotlin.course.calculator.mathCommand

import com.example.kotlin.course.calculator.api.Command
import java.math.BigDecimal

class TimesCommand: Command {
    override fun execute(value1: BigDecimal, value2: BigDecimal): BigDecimal {
        return value1.times(value2)
    }
}
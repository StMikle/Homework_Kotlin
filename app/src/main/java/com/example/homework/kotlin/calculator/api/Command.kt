package com.example.kotlin.course.calculator.api

import java.math.BigDecimal

interface Command {
    fun execute(value1: BigDecimal, value2: BigDecimal) : BigDecimal
}
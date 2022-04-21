package com.github.mkopylec.storage.command.container.domain

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf

@JvmInline
value class WeightValue(val value: BigDecimal) : Comparable<WeightValue> {

    init {
        if (value < ZERO) throw InvalidWeightValue(value)
    }

    operator fun plus(other: WeightValue) = WeightValue(value + other.value)

    operator fun times(multiplicand: Long) = WeightValue(value * valueOf(multiplicand))

    operator fun div(divisor: Long) = WeightValue(value / valueOf(divisor))

    override fun compareTo(other: WeightValue): Int = value.compareTo(other.value)

    override fun toString(): String = value.toString()
}

class InvalidWeightValue(value: BigDecimal) : ContainerBusinessRuleViolation(listOf("value" to value))

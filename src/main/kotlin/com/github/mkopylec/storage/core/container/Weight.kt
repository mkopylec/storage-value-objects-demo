package com.github.mkopylec.storage.core.container

import com.github.mkopylec.storage.core.common.InvariantViolation
import com.github.mkopylec.storage.core.container.Weight.Unit.Kilogram
import com.github.mkopylec.storage.core.container.Weight.Unit.Tonne
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf

class Weight(
    val value: Value,
    val unit: Unit
) : Comparable<Weight> {

    operator fun plus(other: Weight) = Weight(value + other.to(unit).value, unit)

    private fun to(unit: Unit): Weight =
        if (this.unit == unit) this
        else
            when (unit) {
                is Kilogram -> Weight(value * 1000, unit)
                is Tonne -> Weight(value / 1000, unit)
            }

    override fun compareTo(other: Weight): Int = value.compareTo(other.to(unit).value)

    override fun toString(): String = "$value $unit"

    @JvmInline
    value class Value(val value: BigDecimal) : Comparable<Value> {

        init {
            if (value < ZERO) throw InvalidWeightValue(value)
        }

        operator fun plus(other: Value) = Value(value + other.value)

        operator fun times(multiplicand: Long) = Value(value * valueOf(multiplicand))

        operator fun div(divisor: Long) = Value(value / valueOf(divisor))

        override fun compareTo(other: Value): Int = value.compareTo(other.value)

        override fun toString(): String = value.toString()
    }

    sealed class Unit private constructor(val value: String) {

        object Kilogram : Unit("kg")
        object Tonne : Unit("t")

        override fun toString(): String = value

        companion object {

            fun from(value: String): Unit = when (value) {
                Kilogram.value -> Kilogram
                Tonne.value -> Tonne
                else -> throw InvalidWeightUnitValue(value)
            }
        }
    }
}

private class InvalidWeightValue(value: BigDecimal) : InvariantViolation(listOf("value" to value))
private class InvalidWeightUnitValue(value: String) : InvariantViolation(listOf("value" to value))

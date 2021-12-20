package com.github.mkopylec.storage.core.container

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf

class Weight(
    value: BigDecimal,
    unit: String
) : Comparable<Weight> {

    val value = Value(value)
    val unit = Unit.from(unit)

    operator fun plus(other: Weight) = Weight((value + other.to(unit).value).value, unit.value)

    private fun to(unit: Unit): Weight = when (unit) {
        this.unit -> this
        is Unit.Kilogram -> Weight((value * 1000).value, unit.value)
        is Unit.Tonne -> Weight((value / 1000).value, unit.value)
    }

    override fun compareTo(other: Weight): Int = value.compareTo(other.to(unit).value)

    override fun toString(): String = "$value $unit"

    @JvmInline
    value class Value(val value: BigDecimal) : Comparable<Value> {

        init {
            if (value <= ZERO) throw IllegalArgumentException("Invalid weight value: value=$value")
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
                else -> throw IllegalArgumentException("Invalid weight unit: unit=$value")
            }
        }
    }
}
package com.github.mkopylec.storage.core.container

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf

class Weight(
    val value: BigDecimal,
    val unit: String
) : Comparable<Weight> {

    init {
        if (value <= ZERO) throw IllegalArgumentException("Invalid weight value: value=$value")
        if (unit !in listOf(KILOGRAM, TONNE)) throw IllegalArgumentException("Invalid weight unit: unit=$unit")
    }

    operator fun plus(other: Weight) = Weight(value + other.valueTo(unit), unit)

    private fun valueTo(unit: String): BigDecimal = when (unit) {
        this.unit -> value
        KILOGRAM -> value * valueOf(1000)
        else -> value / valueOf(1000)
    }

    override fun compareTo(other: Weight): Int = value.compareTo(other.valueTo(unit))

    override fun toString(): String = "$value $unit"

    companion object Units {

        private const val KILOGRAM = "kg"
        private const val TONNE = "t"
    }
}

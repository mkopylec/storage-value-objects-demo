package com.github.mkopylec.storage.command.container.domain

import com.github.mkopylec.storage.command.container.domain.WeightUnit.Kilogram
import com.github.mkopylec.storage.command.container.domain.WeightUnit.Tonne
import java.math.BigDecimal.ZERO

class Weight(
    val value: WeightValue,
    val unit: WeightUnit
) : Comparable<Weight> {

    constructor() : this(WeightValue(ZERO), Kilogram)

    operator fun plus(other: Weight) = Weight(value + other.to(unit).value, unit)

    fun to(unit: WeightUnit): Weight = when (unit) {
        this.unit -> this
        is Kilogram -> Weight(value * 1000, unit)
        is Tonne -> Weight(value / 1000, unit)
    }

    override fun compareTo(other: Weight): Int = value.compareTo(other.to(unit).value)

    override fun toString(): String = "$value $unit"
}

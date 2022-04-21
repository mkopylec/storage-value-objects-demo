package com.github.mkopylec.storage.command.container.domain

sealed class WeightUnit private constructor(val value: String) {

    object Kilogram : WeightUnit("kg")
    object Tonne : WeightUnit("t")

    override fun toString(): String = value

    companion object {

        fun from(value: String): WeightUnit = when (value) {
            Kilogram.value -> Kilogram
            Tonne.value -> Tonne
            else -> throw InvalidWeightUnit(value)
        }
    }
}

class InvalidWeightUnit(unit: String) : ContainerBusinessRuleViolation(listOf("unit" to unit))

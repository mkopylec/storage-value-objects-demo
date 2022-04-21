package com.github.mkopylec.storage.command.container.domain

@JvmInline
value class Quantity(val value: Int) {

    init {
        if (value < 0) throw InvalidQuantity(value)
    }

    operator fun plus(other: Quantity) = Quantity(value + other.value)

    override fun toString(): String = value.toString()

    companion object {
        val ZERO_QUANTITY = Quantity(0)
    }
}

class InvalidQuantity(value: Int) : ContainerBusinessRuleViolation(listOf("value" to value))

package com.github.mkopylec.storage.core.container

import java.math.BigDecimal
import java.util.UUID
import java.util.UUID.randomUUID

class Item private constructor(
    identifier: UUID,
    name: String,
    weightValue: BigDecimal,
    weightUnit: String
) {

    constructor(name: String, weightValue: BigDecimal, weightUnit: String) : this(randomUUID(), name, weightValue, weightUnit)

    val identifier = Identifier(identifier)
    val name = Name(name)
    val weight = Weight(weightValue, weightUnit)

    override fun toString(): String = "Item(identifier=$identifier, name='$name', weight=$weight)"

    @JvmInline
    value class Identifier(val value: UUID) {

        override fun toString(): String = value.toString()
    }

    @JvmInline
    value class Name(val value: String) {

        override fun toString(): String = value
    }
}

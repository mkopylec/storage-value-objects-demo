package com.github.mkopylec.storage.core.container

import java.math.BigDecimal
import java.util.UUID
import java.util.UUID.randomUUID

class Item private constructor(
    val identifier: UUID,
    val name: String,
    weightValue: BigDecimal,
    weightUnit: String
) {

    constructor(name: String, weightValue: BigDecimal, weightUnit: String) : this(randomUUID(), name, weightValue, weightUnit)

    val weight = Weight(weightValue, weightUnit)

    override fun toString(): String = "Item(identifier=$identifier, name='$name', weight=$weight)"
}

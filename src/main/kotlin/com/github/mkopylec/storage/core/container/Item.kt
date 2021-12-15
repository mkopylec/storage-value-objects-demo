package com.github.mkopylec.storage.core.container

import com.github.mkopylec.storage.core.container.WeightUnits.KILOGRAM
import com.github.mkopylec.storage.core.container.WeightUnits.TONNE
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf
import java.util.UUID
import java.util.UUID.randomUUID

class Item private constructor(
    val identifier: UUID,
    val name: String,
    val weightValue: BigDecimal,
    val weightUnit: String
) {

    constructor(name: String, weightValue: BigDecimal, weightUnit: String) : this(randomUUID(), name, weightValue, weightUnit)

    init {
        if (weightValue <= ZERO) throw IllegalArgumentException("Invalid weight value: value=$weightValue")
        if (weightUnit !in listOf(KILOGRAM, TONNE)) throw IllegalArgumentException("Invalid weight unit: unit=$weightUnit")
    }

    fun weightValueTo(weightUnit: String): BigDecimal = when (weightUnit) {
        this.weightUnit -> weightValue
        KILOGRAM -> weightValue * valueOf(1000)
        else -> weightValue / valueOf(1000)
    }

    override fun toString(): String = "Item(identifier=$identifier, name='$name', weight='$weightValue $weightUnit')"
}

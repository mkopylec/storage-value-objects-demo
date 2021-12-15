package com.github.mkopylec.storage.core.container

import com.github.mkopylec.storage.core.container.WeightUnits.KILOGRAM
import com.github.mkopylec.storage.core.container.WeightUnits.TONNE
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class Container private constructor(
    val identifier: String,
    val maximumWeightValue: BigDecimal,
    val maximumWeightUnit: String,
    items: List<Item>
) {

    constructor(identifier: String, maximumWeightValue: BigDecimal, maximumWeightUnit: String) : this(identifier, maximumWeightValue, maximumWeightUnit, mutableListOf())

    init {
        if (!identifier.matches(identifierPattern)) throw IllegalArgumentException("Invalid container identifier: identifier=$identifier")
        if (maximumWeightValue <= ZERO) throw IllegalArgumentException("Invalid container maximum weight value: value=$maximumWeightValue")
        if (maximumWeightUnit !in listOf(KILOGRAM, TONNE)) throw IllegalArgumentException("Invalid container maximum weight unit: unit=$maximumWeightUnit")
    }

    private val items: MutableList<Item> = items.toMutableList()

    val itemsQuantity: Int
        get() = items.size

    val itemsWeightValue: BigDecimal?
        get() = itemsWeightUnit?.let { unit -> items.map { it.weightValueTo(unit) }.reduce { sum, addend -> sum + addend } }

    val itemsWeightUnit: String?
        get() = if (items.isNotEmpty()) items[0].weightUnit else null

    fun insertItem(item: Item) {
        val weightUnit = itemsWeightUnit
        val weightValue = itemsWeightValue
        if (weightUnit != null && weightValue != null && weightValue + item.weightValueTo(weightUnit) > maximumWeightValue)
            throw IllegalArgumentException(
                "Container maximum weight exceeded: " +
                        "itemIdentifier=${item.identifier}, " +
                        "itemWeight=${item.weightValue} ${item.weightUnit}, " +
                        "identifier=$identifier, " +
                        "itemsWeight=$weightValue $weightUnit, " +
                        "maximumWeight=$maximumWeightValue $maximumWeightUnit"
            )
        items.add(item)
    }

    fun <R> mapItems(transform: (Item) -> R): List<R> = items.map(transform)

    override fun toString(): String = "Container(identifier='$identifier', maximumWeight='$maximumWeightValue $maximumWeightUnit', items=$items)"

    companion object {

        private val identifierPattern = Regex("[A-Z]{8}")
    }
}

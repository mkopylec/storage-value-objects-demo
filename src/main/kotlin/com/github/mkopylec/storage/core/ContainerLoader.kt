package com.github.mkopylec.storage.core

import com.github.mkopylec.storage.core.container.Container
import com.github.mkopylec.storage.core.container.Container.Identifier
import com.github.mkopylec.storage.core.container.Containers
import com.github.mkopylec.storage.core.container.Item
import java.math.BigDecimal
import java.util.UUID

class ContainerLoader(
    private val containers: Containers
) {

    fun loadContainer(containerToLoad: ContainerToLoad): LoadedContainer = try {
        val container = containers.loadContainer(containerToLoad)
        LoadedContainer(container)
    } catch (e: IllegalArgumentException) {
        throw IllegalStateException("Container not loaded", e)
    }
}

class ContainerToLoad(
    identifier: String
) {

    val identifier = Identifier(identifier)

    override fun toString(): String = "ContainerToLoad(identifier=$identifier)"
}

class LoadedContainer private constructor(
    val maximumWeightValue: BigDecimal,
    val maximumWeightUnit: String,
    val itemsQuantity: Int,
    val itemsWeightValue: BigDecimal?,
    val itemsWeightUnit: String?,
    val items: List<InsertedItem>
) {

    constructor(container: Container) : this(
        container.maximumWeight.value.value,
        container.maximumWeight.unit.value,
        container.itemsQuantity.value,
        container.itemsWeight?.value?.value,
        container.itemsWeight?.unit?.value,
        container.mapItems { InsertedItem(it) }
    )

    override fun toString(): String = "LoadedContainer(maximumWeightValue=$maximumWeightValue, maximumWeightUnit='$maximumWeightUnit', itemsQuantity=$itemsQuantity, itemsWeightValue=$itemsWeightValue, itemsWeightUnit=$itemsWeightUnit, items=$items)"

    class InsertedItem private constructor(
        val identifier: UUID,
        val name: String,
        val weightValue: BigDecimal,
        val weightUnit: String
    ) {

        constructor(item: Item) : this(item.identifier.value, item.name.value, item.weight.value.value, item.weight.unit.value)

        override fun toString(): String = "InsertedItem(identifier=$identifier, name='$name', weightValue=$weightValue, weightUnit='$weightUnit')"
    }
}

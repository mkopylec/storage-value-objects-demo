package com.github.mkopylec.storage.core

import com.github.mkopylec.storage.core.LoadedContainer.InsertedItem
import com.github.mkopylec.storage.core.common.InvariantViolation
import com.github.mkopylec.storage.core.container.Container
import com.github.mkopylec.storage.core.container.Container.ItemsQuantity
import com.github.mkopylec.storage.core.container.Containers
import com.github.mkopylec.storage.core.container.Item
import com.github.mkopylec.storage.core.container.Item.Name
import com.github.mkopylec.storage.core.container.Weight.Unit
import com.github.mkopylec.storage.core.container.Weight.Value
import java.math.BigDecimal
import java.util.UUID

class ContainerLoader(
    private val containers: Containers
) {

    fun loadContainer(containerToLoad: ContainerToLoad): LoadedContainer = try {
        val container = containers.loadContainer(containerToLoad)
        LoadedContainer(
            container.maximumWeight.value,
            container.maximumWeight.unit,
            container.itemsQuantity,
            container.itemsWeight?.value,
            container.itemsWeight?.unit,
            items = container.mapItems { InsertedItem(it.identifier, it.name, it.weight.value, it.weight.unit) }
        )
    } catch (e: InvariantViolation) {
        throw ContainerNotLoaded(e)
    }
}

data class ContainerToLoad(
    private val identifier: String
) {

    fun identifier() = Container.Identifier(identifier)
}

data class LoadedContainer private constructor(
    val maximumWeightValue: BigDecimal,
    val maximumWeightUnit: String,
    val itemsQuantity: Int,
    val itemsWeightValue: BigDecimal?,
    val itemsWeightUnit: String?,
    val items: List<InsertedItem>
) {

    constructor(maximumWeightValue: Value, maximumWeightUnit: Unit, itemsQuantity: ItemsQuantity, itemsWeightValue: Value?, itemsWeightUnit: Unit?, items: List<InsertedItem>) :
            this(maximumWeightValue.value, maximumWeightUnit.value, itemsQuantity.value, itemsWeightValue?.value, itemsWeightUnit?.value, items)

    data class InsertedItem private constructor(
        val identifier: UUID,
        val name: String,
        val weightValue: BigDecimal,
        val weightUnit: String
    ) {

        constructor(identifier: Item.Identifier, name: Name, weightValue: Value, weightUnit: Unit) :
                this(identifier.value, name.value, weightValue.value, weightUnit.value)
    }
}

private class ContainerNotLoaded(violation: InvariantViolation) : UseCaseViolation(violation)

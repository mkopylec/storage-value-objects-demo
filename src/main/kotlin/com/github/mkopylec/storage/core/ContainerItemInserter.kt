package com.github.mkopylec.storage.core

import com.github.mkopylec.storage.core.common.InvariantViolation
import com.github.mkopylec.storage.core.container.Container
import com.github.mkopylec.storage.core.container.Containers
import com.github.mkopylec.storage.core.container.Item
import com.github.mkopylec.storage.core.container.Item.Name
import com.github.mkopylec.storage.core.container.Weight
import com.github.mkopylec.storage.core.container.Weight.Unit
import com.github.mkopylec.storage.core.container.Weight.Value
import java.math.BigDecimal
import java.util.UUID

class ContainerItemInserter(
    private val containers: Containers
) {

    fun insertItemInContainer(itemToInsert: ItemToInsert): InsertedItem = try {
        val container = containers.loadContainer(itemToInsert)
        val item = Item(itemToInsert.name(), Weight(itemToInsert.weightValue(), itemToInsert.weightUnit()))
        container.insertItem(item)
        val insertedItem = InsertedItem(item.identifier)
        containers.saveContainer(container)
        insertedItem
    } catch (e: InvariantViolation) {
        throw ContainerItemNotInserted(e)
    }
}

data class ItemToInsert(
    private val name: String,
    private val weightValue: BigDecimal,
    private val weightUnit: String,
    private val containerIdentifier: String
) {

    fun name() = Name(name)
    fun weightValue() = Value(weightValue)
    fun weightUnit() = Unit.from(weightUnit)
    fun containerIdentifier() = Container.Identifier(containerIdentifier)
}

data class InsertedItem private constructor(
    val identifier: UUID
) {

    companion object {

        operator fun invoke(identifier: Item.Identifier) = InsertedItem(identifier.value)
    }
}

private class ContainerItemNotInserted(violation: InvariantViolation) : UseCaseViolation(violation)

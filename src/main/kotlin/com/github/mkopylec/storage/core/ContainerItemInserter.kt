package com.github.mkopylec.storage.core

import com.github.mkopylec.storage.core.common.InvariantViolation
import com.github.mkopylec.storage.core.container.Container.Identifier
import com.github.mkopylec.storage.core.container.Containers
import com.github.mkopylec.storage.core.container.Item
import com.github.mkopylec.storage.core.container.Item.Name
import com.github.mkopylec.storage.core.container.Weight.Unit
import com.github.mkopylec.storage.core.container.Weight.Value
import java.math.BigDecimal
import java.util.UUID

class ContainerItemInserter(
    private val containers: Containers
) {

    fun insertItemInContainer(itemToInsert: ItemToInsert): InsertedItem = try {
        val container = containers.loadContainer(itemToInsert)
        val item = containers.createItem(itemToInsert)
        container.insertItem(item)
        val insertedItem = InsertedItem(item)
        containers.saveContainer(container)
        insertedItem
    } catch (e: InvariantViolation) {
        throw ItemNotInsertedInContainer(e)
    }
}

class ItemToInsert(
    name: String,
    weightValue: BigDecimal,
    weightUnit: String,
    containerIdentifier: String
) {

    val name by lazy { Name(name) }
    val weightValue by lazy { Value(weightValue) }
    val weightUnit by lazy { Unit.from(weightUnit) }
    val containerIdentifier by lazy { Identifier(containerIdentifier) }

    override fun toString(): String = "ItemToInsert(name=$name, weightValue=$weightValue, weightUnit=$weightUnit, containerIdentifier=$containerIdentifier)"
}

class InsertedItem private constructor(
    val identifier: UUID
) {

    constructor(item: Item) : this(item.identifier.value)

    override fun toString(): String = "InsertedItem(identifier=$identifier)"
}

private class ItemNotInsertedInContainer(violation: InvariantViolation) : UseCaseViolation(violation)

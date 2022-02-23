package com.github.mkopylec.storage.core

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
    } catch (e: IllegalArgumentException) {
        throw IllegalStateException("Item not inserted in container", e)
    }
}

class ItemToInsert(
    name: String,
    weightValue: BigDecimal,
    weightUnit: String,
    containerIdentifier: String
) {

    val name = Name(name)
    val weightValue = Value(weightValue)
    val weightUnit = Unit.from(weightUnit)
    val containerIdentifier = Identifier(containerIdentifier)

    override fun toString(): String = "ItemToInsert(name=$name, weightValue=$weightValue, weightUnit=$weightUnit, containerIdentifier=$containerIdentifier)"
}

class InsertedItem private constructor(
    val identifier: UUID
) {

    constructor(item: Item) : this(item.identifier.value)

    override fun toString(): String = "InsertedItem(identifier=$identifier)"
}

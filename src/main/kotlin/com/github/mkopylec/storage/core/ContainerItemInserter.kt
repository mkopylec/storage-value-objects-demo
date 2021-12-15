package com.github.mkopylec.storage.core

import com.github.mkopylec.storage.core.container.Containers
import com.github.mkopylec.storage.core.container.Item
import java.math.BigDecimal
import java.util.UUID

class ContainerItemInserter(
    private val containers: Containers
) {

    fun insertItemInContainer(itemToInsert: ItemToInsert): InsertedItem = try {
        val container = containers.loadContainer(itemToInsert)
        val item = Item(itemToInsert.name, itemToInsert.weightValue, itemToInsert.weightUnit)
        container.insertItem(item)
        val insertedItem = InsertedItem(item.identifier)
        containers.saveContainer(container)
        insertedItem
    } catch (e: IllegalArgumentException) {
        throw IllegalStateException("Item not inserted in container", e)
    }
}

data class ItemToInsert(
    val name: String,
    val weightValue: BigDecimal,
    val weightUnit: String,
    val containerIdentifier: String
)

data class InsertedItem(
    val identifier: UUID
)

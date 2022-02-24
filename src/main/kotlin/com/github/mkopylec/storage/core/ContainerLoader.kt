package com.github.mkopylec.storage.core

import com.github.mkopylec.storage.core.LoadedContainer.InsertedItem
import com.github.mkopylec.storage.core.container.Containers
import java.math.BigDecimal
import java.util.UUID

class ContainerLoader(
    private val containers: Containers
) {

    fun loadContainer(containerToLoad: ContainerToLoad): LoadedContainer = try {
        val container = containers.loadContainer(containerToLoad)
        val maximumWeight = container.maximumWeight
        val itemsWeight = container.itemsWeight
        LoadedContainer(
            maximumWeight.value.value,
            maximumWeight.unit.value,
            container.itemsQuantity.value,
            itemsWeight?.value?.value,
            itemsWeight?.unit?.value,
            items = container.mapItems { InsertedItem(it.identifier.value, it.name.value, it.weight.value.value, it.weight.unit.value) }
        )
    } catch (e: IllegalArgumentException) {
        throw IllegalStateException("Container not loaded", e)
    }
}

data class ContainerToLoad(
    val identifier: String
)

data class LoadedContainer(
    val maximumWeightValue: BigDecimal,
    val maximumWeightUnit: String,
    val itemsQuantity: Int,
    val itemsWeightValue: BigDecimal?,
    val itemsWeightUnit: String?,
    val items: List<InsertedItem>
) {

    data class InsertedItem(
        val identifier: UUID,
        val name: String,
        val weightValue: BigDecimal,
        val weightUnit: String
    )
}

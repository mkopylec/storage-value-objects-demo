package com.github.mkopylec.storage.core.container

import com.github.mkopylec.storage.core.ContainerToAdd
import com.github.mkopylec.storage.core.ContainerToLoad
import com.github.mkopylec.storage.core.ItemToInsert
import com.github.mkopylec.storage.core.container.Container.Identifier

class Containers(
    private val repository: ContainerRepository
) {

    fun createContainer(containerToAdd: ContainerToAdd) = Container(
        containerToAdd.identifier,
        Weight(containerToAdd.maximumWeightValue, containerToAdd.maximumWeightUnit)
    )

    fun createItem(itemToInsert: ItemToInsert) = Item(
        itemToInsert.name,
        Weight(itemToInsert.weightValue, itemToInsert.weightUnit)
    )

    fun saveContainer(container: Container) = repository.save(container)

    fun loadContainer(itemToInsert: ItemToInsert): Container = loadContainer(itemToInsert.containerIdentifier)

    fun loadContainer(containerToLoad: ContainerToLoad): Container = loadContainer(containerToLoad.identifier)

    private fun loadContainer(identifier: Identifier): Container = repository.findByIdentifier(identifier) ?: throw IllegalArgumentException("Missing container: identifier=$identifier")
}

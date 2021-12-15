package com.github.mkopylec.storage.core.container

import com.github.mkopylec.storage.core.ContainerToAdd
import com.github.mkopylec.storage.core.ContainerToLoad
import com.github.mkopylec.storage.core.ItemToInsert

class Containers(
    private val repository: ContainerRepository
) {

    fun createContainer(containerToAdd: ContainerToAdd) = Container(containerToAdd.identifier, containerToAdd.maximumWeightValue, containerToAdd.maximumWeightUnit)

    fun saveContainer(container: Container) = repository.save(container)

    fun loadContainer(itemToInsert: ItemToInsert): Container = loadContainer(itemToInsert.containerIdentifier)

    fun loadContainer(containerToLoad: ContainerToLoad): Container = loadContainer(containerToLoad.identifier)

    private fun loadContainer(identifier: String): Container = repository.findByIdentifier(identifier) ?: throw IllegalArgumentException("Missing container: identifier=$identifier")
}

package com.github.mkopylec.storage.core.container

import com.github.mkopylec.storage.core.ContainerToAdd
import com.github.mkopylec.storage.core.ContainerToLoad
import com.github.mkopylec.storage.core.ItemToInsert
import com.github.mkopylec.storage.core.container.Container.Identifier
import com.github.mkopylec.storage.core.container.Item.Name
import com.github.mkopylec.storage.core.container.Weight.Unit
import com.github.mkopylec.storage.core.container.Weight.Value

class Containers(
    private val repository: ContainerRepository
) {

    fun createContainer(containerToAdd: ContainerToAdd) = Container(
        Identifier(containerToAdd.identifier),
        Weight(Value(containerToAdd.maximumWeightValue), Unit.from(containerToAdd.maximumWeightUnit))
    )

    fun createItem(itemToInsert: ItemToInsert) = Item(
        Name(itemToInsert.name),
        Weight(Value(itemToInsert.weightValue), Unit.from(itemToInsert.weightUnit))
    )

    fun saveContainer(container: Container) = repository.save(container)

    fun loadContainer(itemToInsert: ItemToInsert): Container = loadContainer(Identifier(itemToInsert.containerIdentifier))

    fun loadContainer(containerToLoad: ContainerToLoad): Container = loadContainer(Identifier(containerToLoad.identifier))

    private fun loadContainer(identifier: Identifier): Container = repository.findByIdentifier(identifier) ?: throw IllegalArgumentException("Missing container: identifier=$identifier")
}

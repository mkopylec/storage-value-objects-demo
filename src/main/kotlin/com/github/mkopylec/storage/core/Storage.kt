package com.github.mkopylec.storage.core

class Storage(
    private val containerAdder: ContainerAdder,
    private val containerItemInserter: ContainerItemInserter,
    private val containerLoader: ContainerLoader
) {

    fun addContainer(containerToAdd: ContainerToAdd) = containerAdder.addContainer(containerToAdd)

    fun insertItemInContainer(itemToInsert: ItemToInsert): InsertedItem = containerItemInserter.insertItemInContainer(itemToInsert)

    fun loadContainer(containerToLoad: ContainerToLoad): LoadedContainer = containerLoader.loadContainer(containerToLoad)
}

package com.github.mkopylec.storage.query

import com.github.mkopylec.storage.command.common.domain.events.EventHandler
import com.github.mkopylec.storage.command.container.domain.events.ContainerAdded
import com.github.mkopylec.storage.command.container.domain.events.ItemInsertedInContainer
import com.github.mkopylec.storage.query.ContainerViewDocument.Companion.CONTAINER_IDENTIFIER_FIELD
import com.github.mkopylec.storage.query.ContainerViewDocument.Companion.CONTAINER_ITEMS_FIELD
import com.github.mkopylec.storage.query.ContainerViewDocument.ItemViewDocument
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component

@Component
class ContainerAddedHandler(
    private val updater: ContainerUpdater
) : EventHandler<ContainerAdded>(ContainerAdded::class) {

    override suspend fun handle(event: ContainerAdded) = updater.execute { it.insert(ContainerViewDocument(event)) }
}

@Component
class ItemInsertedInContainerHandler(
    private val updater: ContainerUpdater
) : EventHandler<ItemInsertedInContainer>(ItemInsertedInContainer::class) {

    override suspend fun handle(event: ItemInsertedInContainer) =
        updater.execute {
            it.updateFirst(
                query(where(CONTAINER_IDENTIFIER_FIELD).isEqualTo(event.containerIdentifier.value)),
                Update().push(CONTAINER_ITEMS_FIELD, ItemViewDocument(event)),
                ContainerViewDocument::class.java
            )
        }
}

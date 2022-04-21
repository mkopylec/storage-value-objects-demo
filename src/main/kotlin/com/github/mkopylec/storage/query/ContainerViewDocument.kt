package com.github.mkopylec.storage.query

import com.github.mkopylec.storage.command.container.domain.WeightUnit.Kilogram
import com.github.mkopylec.storage.command.container.domain.events.ContainerAdded
import com.github.mkopylec.storage.command.container.domain.events.ItemInsertedInContainer
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.UUID

@Document("containerViews")
class ContainerViewDocument private constructor(
    @Id
    val identifier: String,
    @Indexed(background = true)
    val maximumWeightInKilograms: BigDecimal,
    val items: List<ItemViewDocument>
) {
    constructor(containerAdded: ContainerAdded) : this(
        identifier = containerAdded.identifier.value,
        maximumWeightInKilograms = containerAdded.maximumWeight.to(Kilogram).value.value,
        items = emptyList()
    )

    companion object {
        const val CONTAINER_IDENTIFIER_FIELD = "_id"
        const val CONTAINER_MAXIMUM_WEIGHT_IN_KILOGRAMS_FIELD = "maximumWeightInKilograms"
        const val CONTAINER_ITEMS_FIELD = "items"
    }

    class ItemViewDocument(
        val identifier: UUID,
        val name: String,
        val weightInKilograms: BigDecimal
    ) {
        constructor(itemInsertedInContainer: ItemInsertedInContainer) : this(
            identifier = itemInsertedInContainer.identifier.value,
            name = itemInsertedInContainer.name.value,
            weightInKilograms = itemInsertedInContainer.weight.to(Kilogram).value.value
        )

        companion object {
            const val ITEM_IDENTIFIER_FIELD = "identifier"
            const val ITEM_NAME_FIELD = "name"
            const val ITEM_WEIGHT_IN_KILOGRAMS_FIELD = "weightInKilograms"
        }
    }
}

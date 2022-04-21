package com.github.mkopylec.storage.query

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY
import com.github.mkopylec.storage.query.ContainerDocument.ItemDocument
import com.github.mkopylec.storage.query.ContainerViewDocument.ItemViewDocument
import java.math.BigDecimal
import java.util.UUID

@JsonAutoDetect(fieldVisibility = ANY)
class ContainerView private constructor(
    private val identifier: String,
    private val maximumWeightInKilograms: BigDecimal,
    private val itemsQuantity: Int,
    private val itemsWeightInKilograms: BigDecimal,
    private val items: List<ItemView>,
) {
    constructor(document: ContainerViewDocument) : this(
        identifier = document.identifier,
        maximumWeightInKilograms = document.maximumWeightInKilograms,
        itemsQuantity = document.items.size,
        itemsWeightInKilograms = document.items.sumOf { it.weightInKilograms },
        items = document.items.map { ItemView(it) }
    )

    constructor(document: ContainerDocument) : this(
        identifier = document.identifier,
        maximumWeightInKilograms = document.maximumWeight.valueInKilograms,
        itemsQuantity = document.items.items.size,
        itemsWeightInKilograms = document.items.weight.valueInKilograms,
        items = document.items.items.map { ItemView(it) }
    )

    @JsonAutoDetect(fieldVisibility = ANY)
    class ItemView private constructor(
        private val identifier: UUID,
        private val name: String,
        private val weightInKilograms: BigDecimal
    ) {

        constructor(document: ItemViewDocument) : this(
            identifier = document.identifier,
            name = document.name,
            weightInKilograms = document.weightInKilograms
        )

        constructor(document: ItemDocument) : this(
            identifier = document.identifier,
            name = document.name,
            weightInKilograms = document.weight.valueInKilograms
        )
    }
}

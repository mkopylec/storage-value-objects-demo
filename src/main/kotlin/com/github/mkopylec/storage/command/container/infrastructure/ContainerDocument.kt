package com.github.mkopylec.storage.command.container.infrastructure

import com.github.mkopylec.storage.command.common.domain.AggregateVersion
import com.github.mkopylec.storage.command.container.domain.Container
import com.github.mkopylec.storage.command.container.domain.ContainerIdentifier
import com.github.mkopylec.storage.command.container.domain.Item
import com.github.mkopylec.storage.command.container.domain.ItemIdentifier
import com.github.mkopylec.storage.command.container.domain.ItemName
import com.github.mkopylec.storage.command.container.domain.Items
import com.github.mkopylec.storage.command.container.domain.Weight
import com.github.mkopylec.storage.command.container.domain.WeightUnit
import com.github.mkopylec.storage.command.container.domain.WeightUnit.Kilogram
import com.github.mkopylec.storage.command.container.domain.WeightValue
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.UUID

@Document("containers")
class ContainerDocument private constructor(
    @Id
    private val identifier: String,
    @Version
    private var version: Int, // Must be var, otherwise saving the document will fail.
    private val maximumWeight: WeightDocument,
    private val items: ItemsDocument
) {
    constructor(container: Container) : this(
        container.identifier.value,
        container.version.value,
        WeightDocument(container.maximumWeight),
        ItemsDocument(container.items)
    )

    fun toContainer() = Container.fromPersistentState(ContainerIdentifier(identifier), AggregateVersion(version), maximumWeight.toWeight(), items.toItems())

    private class WeightDocument private constructor(
        private val value: BigDecimal,
        private val unit: String,
        private val valueInKilograms: BigDecimal // Not needed for immediately consistent read model
    ) {
        constructor(weight: Weight) : this(weight.value.value, weight.unit.value, weight.to(Kilogram).value.value)

        fun toWeight() = Weight(WeightValue(value), WeightUnit.from(unit))
    }

    private class ItemsDocument private constructor(
        private val items: List<ItemDocument>,
        private val weight: WeightDocument
    ) {
        constructor(items: Items) : this(items.map { ItemDocument(it) }, WeightDocument(items.weight))

        fun toItems() = Items.fromPersistentState(items.map { it.toItem() }, weight.toWeight())
    }

    private class ItemDocument private constructor(
        private val identifier: UUID,
        private val name: String,
        private val weight: WeightDocument
    ) {
        constructor(item: Item) : this(item.identifier.value, item.name.value, WeightDocument(item.weight))

        fun toItem() = Item.fromPersistentState(ItemIdentifier(identifier), ItemName(name), weight.toWeight())
    }
}

package com.github.mkopylec.storage.core.container

class Container private constructor(
    val identifier: Identifier,
    val maximumWeight: Weight,
    items: List<Item>
) {

    constructor(identifier: Identifier, maximumWeight: Weight) : this(identifier, maximumWeight, mutableListOf())

    private val items: MutableList<Item> = items.toMutableList()

    val itemsQuantity: ItemsQuantity
        get() = ItemsQuantity(items.size)

    val itemsWeight: Weight?
        get() = items.map { it.weight }.reduceOrNull { sum, addend -> sum + addend }

    fun insertItem(item: Item) {
        val weight = itemsWeight
        if (weight != null && weight + item.weight > maximumWeight)
            throw IllegalArgumentException(
                "Container maximum weight exceeded: " +
                        "itemIdentifier=${item.identifier}, " +
                        "itemWeight=${item.weight}, " +
                        "identifier=$identifier, " +
                        "itemsWeight=$weight, " +
                        "maximumWeight=$maximumWeight"
            )
        items.add(item)
    }

    fun <R> mapItems(transform: (Item) -> R): List<R> = items.map(transform)

    override fun toString(): String = "Container(identifier='$identifier', maximumWeight=$maximumWeight, items=$items)"

    @JvmInline
    value class Identifier(val value: String) {

        init {
            if (!value.matches(pattern)) throw IllegalArgumentException("Invalid container identifier: identifier=$value")
        }

        override fun toString(): String = value

        companion object {

            private val pattern = Regex("[A-Z]{8}")
        }
    }

    @JvmInline
    value class ItemsQuantity(val value: Int) {

        override fun toString(): String = value.toString()
    }
}

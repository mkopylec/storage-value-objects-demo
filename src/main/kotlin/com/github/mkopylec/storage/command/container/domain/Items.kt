package com.github.mkopylec.storage.command.container.domain

class Items private constructor(
    values: List<Item>,
    weight: Weight
) {
    constructor() : this(emptyList(), Weight())

    private val values: MutableList<Item> = values.toMutableList()

    var weight: Weight = weight
        private set

    fun insert(item: Item) {
        values.add(item)
        weight += item.weight

    }

    fun <R> map(transform: (Item) -> R): List<R> = values.map(transform)

    override fun toString(): String = "Items(values=$values, weight=$weight)"

    companion object {
        fun fromPersistentState(values: List<Item>, weight: Weight) = Items(values, weight)
    }
}

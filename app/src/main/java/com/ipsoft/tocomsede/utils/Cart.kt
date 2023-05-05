package com.ipsoft.tocomsede.utils

import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.model.Item

object Cart {
    private val items = linkedSetOf<Pair<Item, Int>>()

    fun getTotal() = items.sumOf { it.first.price * it.second }.toString().toCurrency()

    fun addItem(item: Item, quantity: Int) {
        items.add(item to quantity)
    }

    fun removeItem(item: Item) {
        items.removeIf { it.first.id == item.id }
    }

    fun getItems() = items
    fun clearCart() {
        items.clear()
    }
}

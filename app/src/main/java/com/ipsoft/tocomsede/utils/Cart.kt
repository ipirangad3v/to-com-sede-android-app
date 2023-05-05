package com.ipsoft.tocomsede.utils

import com.ipsoft.tocomsede.core.model.Item

object Cart {
    private val items = mutableListOf<Item>()

    fun addItem(item: Item) {
        items.add(item)
    }

    fun removeItem(item: Item) {
        items.remove(item)
    }

    fun getItems() = items.toList()

    fun clearCart() {
        items.clear()
    }
}
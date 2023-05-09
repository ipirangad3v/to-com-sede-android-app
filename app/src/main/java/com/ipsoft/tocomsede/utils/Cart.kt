package com.ipsoft.tocomsede.utils

import android.util.Log
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.model.Item

object Cart {
    private val listeners = mutableListOf<CartListener>()
    private val items = linkedSetOf<Pair<Item, Int>>()

    fun getTotal() = items.sumOf { it.first.price * it.second }.toString().toCurrency()

    fun addItem(item: Item, quantity: Int) {
        var previousQuantity = 0

        Log.d("Cart", "addItem: $item - $quantity")

        items.find { it.first.id == item.id }?.let {
            previousQuantity = it.second
            items.remove(it)
        }
        items.add(item to quantity + previousQuantity)

        notifyListeners()
    }

    fun removeItem(item: Item) {
        items.removeIf { it.first.id == item.id }

        notifyListeners()
    }

    fun getItems() = items
    fun clearCart() {
        items.clear()
        notifyListeners()
    }

    private fun notifyListeners() {
        listeners.forEach { it.onCartChanged() }
    }

    interface CartListener {
        fun onCartChanged()
    }

    fun addListener(listener: CartListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: CartListener) {
        listeners.remove(listener)
    }

    fun getItemsCount(): Int = items.size
    fun checkIfItemIsInCartAndReturnQuantity(item: Item): Int {
        return items.find { it.first.id == item.id }?.second ?: 0
    }
}

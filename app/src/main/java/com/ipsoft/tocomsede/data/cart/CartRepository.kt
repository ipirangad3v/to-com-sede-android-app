package com.ipsoft.tocomsede.data.cart

import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addItemToCart(item: Item, quantity: Int): ResultState<Boolean>
    suspend fun getCartItems(): Flow<ResultState<LinkedHashSet<Pair<Item, Int>>>>
    suspend fun removeItemFromCart(item: Item): ResultState<Boolean>
    suspend fun clearCart(): ResultState<Boolean>
    suspend fun getCartTotal(): ResultState<String>
    fun getCartItemsCount(): Int
    suspend fun checkIfItemIsInCartAndReturnQuantity(item: Item): Int
}

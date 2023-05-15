package com.ipsoft.tocomsede.data.cart

import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.Change
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.PaymentMethod
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addItemToCart(
        item: Item,
        quantity: Int,
    ): ResultState<Boolean>

    suspend fun getCartItems(): Flow<ResultState<List<Item>>>
    suspend fun removeItemFromCart(item: Item): ResultState<Boolean>
    suspend fun clearCart(): ResultState<Boolean>
    suspend fun getCartTotal(): ResultState<String>
    fun getCartItemsCount(): Int
    suspend fun checkIfItemIsInCartAndReturnQuantity(item: Item): Int
    suspend fun checkout(
        address: Address,
        paymentMethod: PaymentMethod,
        change: Change,
    ): Flow<ResultState<Boolean>>
}

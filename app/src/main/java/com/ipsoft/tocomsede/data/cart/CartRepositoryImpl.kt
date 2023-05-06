package com.ipsoft.tocomsede.data.cart

import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.utils.Cart
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cart: Cart) : CartRepository {

    override suspend fun addItemToCart(item: Item, quantity: Int): ResultState<Boolean> {
        cart.addItem(item, quantity)
        return ResultState.Success(true)
    }

    override suspend fun getCartItems(): Flow<ResultState<LinkedHashSet<Pair<Item, Int>>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            trySend(ResultState.Success(cart.getItems()))
            awaitClose { close() }
        }

    override suspend fun removeItemFromCart(item: Item): ResultState<Boolean> {
        cart.removeItem(item)
        return ResultState.Success(true)
    }

    override suspend fun clearCart(): ResultState<Boolean> {
        cart.clearCart()
        return ResultState.Success(true)
    }

    override suspend fun getCartTotal(): ResultState<String> = ResultState.Success(cart.getTotal())
}

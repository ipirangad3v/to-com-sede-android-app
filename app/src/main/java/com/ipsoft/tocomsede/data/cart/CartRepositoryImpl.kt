package com.ipsoft.tocomsede.data.cart

import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.utils.Cart

class CartRepositoryImpl: CartRepository {

    private val cart = Cart

    override suspend fun addItemToCart(item: Item, quantity: Int): ResultState<Boolean> {
        cart.addItem(item, quantity)
        return ResultState.Success(true)
    }

    override suspend fun getCartItems(): ResultState<LinkedHashSet<Pair<Item, Int>>> {
        return ResultState.Success(cart.getItems())
    }

    override suspend fun removeItemFromCart(item: Item): ResultState<Boolean> {
        cart.removeItem(item)
        return ResultState.Success(true)
    }

    override suspend fun clearCart(): ResultState<Boolean> {
        cart.clearCart()
        return ResultState.Success(true)
    }
}
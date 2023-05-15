package com.ipsoft.tocomsede.data.cart

import com.google.firebase.database.DatabaseReference
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.PaymentMethod
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.utils.Cart
import com.ipsoft.tocomsede.utils.UserInfo.userUid
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cart: Cart,
    private val dbReference: DatabaseReference
) : CartRepository {

    private val ordersReference
        get() = dbReference.child("users").child(userUid ?: "").child("orders")

    override suspend fun addItemToCart(item: Item, quantity: Int): ResultState<Boolean> {
        cart.addItem(item, quantity)
        return ResultState.Success(true)
    }

    override suspend fun getCartItems(): Flow<ResultState<List<Item>>> = callbackFlow {
        trySend(ResultState.Loading)
        trySend(ResultState.Success(cart.getItems().toList()))
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
    override fun getCartItemsCount(): Int = cart.getItemsCount()
    override suspend fun checkIfItemIsInCartAndReturnQuantity(item: Item): Int {
        return cart.checkIfItemIsInCartAndReturnQuantity(item)
    }

    override suspend fun checkout(
        address: Address,
        paymentMethod: PaymentMethod
    ): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        if (userUid == null) {
            trySend(ResultState.Failure(Exception("Usuário não logado")))
            return@callbackFlow
        } else {
            val order =
                Order(cart.getItems().toList(), address = address, paymentMethod = paymentMethod)
            ordersReference.push().setValue(order).addOnCompleteListener {
                if (it.isSuccessful) {
                    cart.clearCart()
                    trySend(ResultState.Success(true))
                } else {
                    trySend(
                        ResultState.Failure(
                            it.exception ?: Exception("Erro ao finalizar compra")
                        )
                    )
                }
            }
        }
        awaitClose { close() }
    }
}

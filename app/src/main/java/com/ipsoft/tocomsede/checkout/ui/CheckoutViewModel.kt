package com.ipsoft.tocomsede.checkout.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.ui.state.CartItemState
import com.ipsoft.tocomsede.data.cart.CartRepository
import com.ipsoft.tocomsede.utils.Cart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val cartRepository: CartRepository) :
    ViewModel(), Cart.CartListener {

    private val _cartItemState: MutableState<CartItemState> = mutableStateOf(CartItemState())
    val cartItemState: State<CartItemState> = _cartItemState

    private val _cartTotalState: MutableState<String> = mutableStateOf("")
    val cartTotalState: State<String> = _cartTotalState

    init {
        Cart.addListener(this)
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            cartRepository.getCartTotal().let { result ->
                if (result is ResultState.Success) {
                    _cartTotalState.value = result.data
                }
            }
            cartRepository.getCartItems().collect {
                _cartItemState.value = when (it) {
                    is ResultState.Success -> {
                        CartItemState(items = it.data.toList())
                    }

                    is ResultState.Failure -> {
                        CartItemState(error = it.msg.toString())
                    }

                    ResultState.Loading -> {
                        CartItemState(isLoading = true)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Cart.removeListener(this)
    }

    override fun onCartChanged() {
        loadCart()
    }
}

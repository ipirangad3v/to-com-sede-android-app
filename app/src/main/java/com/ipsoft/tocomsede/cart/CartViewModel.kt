package com.ipsoft.tocomsede.cart

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.ResultState.Failure
import com.ipsoft.tocomsede.core.model.ResultState.Loading
import com.ipsoft.tocomsede.core.model.ResultState.Success
import com.ipsoft.tocomsede.data.cart.CartRepository
import com.ipsoft.tocomsede.utils.Cart
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) :
    ViewModel(),
    Cart.CartListener {

    private val _cartItemState: MutableState<CartItemState> = mutableStateOf(CartItemState())
    val cartItemState: State<CartItemState> = _cartItemState

    private val _cartTotalState: MutableState<String> = mutableStateOf("")
    val cartTotalState: State<String> = _cartTotalState

    init {
        Cart.addListener(this)
        loadCart()
    }

    override fun onCleared() {
        super.onCleared()
        Cart.removeListener(this)
    }

    fun loadCart() {
        viewModelScope.launch {
            cartRepository.getCartTotal().let { result ->
                if (result is Success) {
                    _cartTotalState.value = result.data
                }
            }
            cartRepository.getCartItems().collect {
                _cartItemState.value = when (it) {
                    is Success -> {
                        CartItemState(item = it.data.toList())
                    }

                    is Failure -> {
                        CartItemState(error = it.msg.toString())
                    }

                    Loading -> {
                        CartItemState(isLoading = true)
                    }
                }
            }
        }
    }

    data class CartItemState(
        val item: List<Pair<Item, Int>> = emptyList(),
        val error: String? = null,
        val isLoading: Boolean = false
    )

    override fun onCartChanged() {
        loadCart()
    }
}

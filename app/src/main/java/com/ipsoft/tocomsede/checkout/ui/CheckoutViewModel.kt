package com.ipsoft.tocomsede.checkout.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.ui.state.CartItemState
import com.ipsoft.tocomsede.data.cart.CartRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.address.RealtimeAddressRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.phone.RealtimePhoneRepository
import com.ipsoft.tocomsede.utils.Cart
import com.ipsoft.tocomsede.utils.UserInfo
import com.ipsoft.tocomsede.utils.UserInfo.isUserLogged
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val addressRepository: RealtimeAddressRepository,
    private val phoneRepository: RealtimePhoneRepository
) :
    ViewModel(), Cart.CartListener, UserInfo.UserInfoListener {

    private val _userLoggedState: MutableState<Boolean> = mutableStateOf(isUserLogged)
    val userLoggedState: State<Boolean> = _userLoggedState

    private val _cartItemState: MutableState<CartItemState> = mutableStateOf(CartItemState())
    val cartItemState: State<CartItemState> = _cartItemState

    private val _favoriteAddressState: MutableState<Address?> = mutableStateOf(Address())
    val favoriteAddressState: State<Address?> = _favoriteAddressState

    private val _cartTotalState: MutableState<String> = mutableStateOf("")
    val cartTotalState: State<String> = _cartTotalState

    private val _phoneState: MutableState<String?> = mutableStateOf(null)
    val phoneState: State<String?> = _phoneState

    init {
        UserInfo.addListener(this)
        Cart.addListener(this)
        loadCart()
        loadFavoriteAddress()
        loadPhone()
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

    fun loadFavoriteAddress() {
        viewModelScope.launch {
            addressRepository.getFavoriteAddress().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _favoriteAddressState.value = result.data
                    }

                    else -> {
                        _favoriteAddressState.value = null
                    }
                }
            }
        }
    }

    fun loadPhone() {
        viewModelScope.launch {
            phoneRepository.getPhone().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _phoneState.value = result.data
                    }

                    else -> {
                        _phoneState.value = null
                    }
                }
            }
        }
    }

    fun checkout() {
        viewModelScope.launch {
            _favoriteAddressState.value?.let { address ->
                cartRepository.checkout(address).collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _cartItemState.value = CartItemState(checkoutSuccess = true)
                        }

                        is ResultState.Failure -> {
                            _cartItemState.value = CartItemState(error = result.msg.toString())
                        }

                        ResultState.Loading -> {
                            _cartItemState.value = CartItemState(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Cart.removeListener(this)
        UserInfo.removeListener(this)
    }

    override fun onCartChanged() {
        loadCart()
    }

    override fun onUserInfoChanged(isUserLogged: Boolean) {
        _userLoggedState.value = isUserLogged
    }
}

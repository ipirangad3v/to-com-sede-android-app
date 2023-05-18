package com.ipsoft.tocomsede.feature.cart

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.base.ui.state.CartItemState
import com.ipsoft.tocomsede.core.extensions.currencyToDouble
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.Change
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.PaymentMethod
import com.ipsoft.tocomsede.core.model.ResultState.Failure
import com.ipsoft.tocomsede.core.model.ResultState.Loading
import com.ipsoft.tocomsede.core.model.ResultState.Success
import com.ipsoft.tocomsede.core.model.Store
import com.ipsoft.tocomsede.core.utils.UserInfo
import com.ipsoft.tocomsede.data.cart.CartRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.address.RealtimeAddressRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.phone.RealtimePhoneRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.store.RealtimeStoreRepository
import com.ipsoft.tocomsede.utils.Cart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val addressRepository: RealtimeAddressRepository,
    private val phoneRepository: RealtimePhoneRepository,
    private val storeRepository: RealtimeStoreRepository
) :
    ViewModel(),
    Cart.CartListener,
    UserInfo.UserInfoListener {

    private val _store = mutableStateOf(Store())
    val store: State<Store> = _store

    private val _cartItemState: MutableState<CartItemState> = mutableStateOf(CartItemState())
    val cartItemState: State<CartItemState> = _cartItemState

    private val _paymentState: MutableState<PaymentMethod> = mutableStateOf(PaymentMethod.MONEY)
    val paymentState: State<PaymentMethod> = _paymentState

    private val _changePaymentState: MutableState<Change> = mutableStateOf(Change())
    val changePaymentState: State<Change> = _changePaymentState

    private val _cartTotalState: MutableState<String> = mutableStateOf("")
    val cartTotalState: State<String> = _cartTotalState

    private val _userLoggedState: MutableState<Boolean> = mutableStateOf(UserInfo.isUserLogged)
    val userLoggedState: State<Boolean> = _userLoggedState

    private val _favoriteAddressState: MutableState<Address?> = mutableStateOf(
        Address()
    )
    val favoriteAddressState: State<Address?> = _favoriteAddressState

    private val _phoneState: MutableState<String?> = mutableStateOf(null)
    val phoneState: State<String?> = _phoneState

    private val _isChangeCorrect: MutableState<Boolean> =
        mutableStateOf(_changePaymentState.value.changeFor > _cartTotalState.value.currencyToDouble())
    val isChangeCorrect: State<Boolean> = _isChangeCorrect

    init {
        UserInfo.addListener(this)
        Cart.addListener(this)
        loadCart()
        loadFavoriteAddress()
        loadPhone()
        loadStore()
    }

    private fun loadStore() {
        viewModelScope.launch {
            storeRepository.getStore().collect { result ->
                when (result) {
                    is Success -> {
                        _store.value = result.data
                    }

                    else -> {
                        _store.value = Store()
                    }
                }
            }
        }
    }

    fun loadFavoriteAddress() {
        viewModelScope.launch {
            addressRepository.getFavoriteAddress().collect { result ->
                when (result) {
                    is Success -> {
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
                    is Success -> {
                        _phoneState.value = result.data
                    }

                    else -> {
                        _phoneState.value = null
                    }
                }
            }
        }
    }

    fun removeItem(item: Item) {
        viewModelScope.launch {
            cartRepository.removeItemFromCart(item)
        }
    }

    fun checkout() {
        viewModelScope.launch {
            _favoriteAddressState.value?.let { address ->
                cartRepository.checkout(address, _paymentState.value, _changePaymentState.value)
                    .collect { result ->
                        when (result) {
                            is Success -> {
                                _cartItemState.value = CartItemState(checkoutSuccess = true)
                            }

                            is Failure -> {
                                _cartItemState.value = CartItemState(error = result.msg.toString())
                            }

                            Loading -> {
                                _cartItemState.value = CartItemState(isLoading = true)
                            }
                        }
                    }
            }
        }
    }

    fun updatePaymentMethod(paymentMethod: PaymentMethod) {
        _paymentState.value = paymentMethod
    }

    override fun onCleared() {
        super.onCleared()
        Cart.removeListener(this)
        UserInfo.removeListener(this)
    }

    override fun onCartChanged() {
        loadCart()
    }

    private fun updateChangeIsCorrect(change: Change) {
        _isChangeCorrect.value =
            change.changeFor > _cartTotalState.value.currencyToDouble()
    }

    override fun onUserInfoChanged(isUserLogged: Boolean) {
        _userLoggedState.value = isUserLogged
    }

    init {
        Cart.addListener(this)
        loadCart()
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
                        CartItemState(items = it.data)
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

    fun updateChange(change: Change) {
        _changePaymentState.value = change
        updateChangeIsCorrect(change)
    }
}

package com.ipsoft.tocomsede.feature.orders.orderslist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.ResultState.Failure
import com.ipsoft.tocomsede.core.model.ResultState.Success
import com.ipsoft.tocomsede.core.utils.UserInfo
import com.ipsoft.tocomsede.data.firebaserealtimedb.orders.RealtimeOrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val ordersRepository: RealtimeOrdersRepository) :
    ViewModel(), UserInfo.UserInfoListener {

    private val _state = mutableStateOf(OrdersState())
    val state: State<OrdersState> = _state

    private val _userLoggedState: MutableState<Boolean> = mutableStateOf(UserInfo.isUserLogged)
    val userLoggedState: State<Boolean> = _userLoggedState

    init {
        UserInfo.addListener(this)
        if (UserInfo.isUserLogged) {
            getOrders()
        }
    }

    override fun onCleared() {
        super.onCleared()
        UserInfo.removeListener(this)
    }

    private fun getOrders() {
        viewModelScope.launch {
            ordersRepository.getOrders().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _state.value = OrdersState(isLoading = true)
                    }

                    is Success -> {
                        _state.value = OrdersState(orders = it.data, isLoading = false)
                    }

                    is Failure -> {
                        _state.value = OrdersState(error = it.msg, isLoading = false)
                    }
                }
            }
        }
    }

    data class OrdersState(
        val orders: List<Order> = emptyList(),
        val isLoading: Boolean = false,
        val error: Throwable? = null
    )

    override fun onUserInfoChanged(isUserLogged: Boolean) {
        _userLoggedState.value = isUserLogged
        if (isUserLogged) {
            getOrders()
        }
    }
}

package com.ipsoft.tocomsedeadmin.feature.orderdetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsedeadmin.data.orders.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(private val ordersRepository: OrdersRepository) :
    ViewModel() {

    private val _orderState = mutableStateOf(OrderDetailScreenState())
    val orderState: State<OrderDetailScreenState> = _orderState

    fun getOrderById(orderId: String) {
        viewModelScope.launch {
            ordersRepository.getOrderById(orderId).collect {
                when (it) {
                    is ResultState.Success -> {
                        _orderState.value =
                            OrderDetailScreenState(order = it.data, isLoading = false)
                    }

                    is ResultState.Failure -> {
                        _orderState.value =
                            OrderDetailScreenState(error = it.msg.toString(), isLoading = false)
                    }

                    ResultState.Loading -> {
                        _orderState.value = OrderDetailScreenState(isLoading = true)
                    }
                }
            }
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            ordersRepository.updateOrder(order).collect {
                when (it) {
                    is ResultState.Success -> {
                        _orderState.value = OrderDetailScreenState(order = it.data, isLoading = false)
                    }

                    is ResultState.Failure -> {
                        _orderState.value = OrderDetailScreenState(error = it.msg.toString(), isLoading = false)
                    }

                    ResultState.Loading -> {
                        _orderState.value = OrderDetailScreenState(isLoading = true)
                    }
                }
            }
        }
    }

    data class OrderDetailScreenState(
        val order: Order? = null,
        val error: String? = null,
        val isLoading: Boolean = true
    )
}

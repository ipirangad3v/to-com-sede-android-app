package com.ipsoft.tocomsedeadmin.feature.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.Store
import com.ipsoft.tocomsede.core.utils.UserInfo
import com.ipsoft.tocomsedeadmin.data.orders.OrdersRepository
import com.ipsoft.tocomsedeadmin.data.store.RealtimeStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val storeRepository: RealtimeStoreRepository

) : ViewModel(), UserInfo.UserInfoListener {

    private val _homeState = mutableStateOf(HomeState())
    val homeState: State<HomeState> = _homeState

    init {
        getOrders()
        getStore()
    }

    private fun getStore() {
        viewModelScope.launch {
            storeRepository.getStore().collect {
                when (it) {
                    is ResultState.Success -> {
                        _homeState.value = _homeState.value.copy(store = it.data, loading = false)
                    }

                    is ResultState.Failure -> {
                        _homeState.value =
                            _homeState.value.copy(error = it.msg.toString(), loading = false)
                    }

                    ResultState.Loading -> {
                        _homeState.value = _homeState.value.copy(loading = true)
                    }
                }
            }
        }
    }

    private fun getOrders() {
        viewModelScope.launch {
            ordersRepository.getOrders().collect {
                when (it) {
                    is ResultState.Success -> {
                        _homeState.value = _homeState.value.copy(orders = it.data, loading = false)
                    }

                    is ResultState.Failure -> {
                        _homeState.value =
                            _homeState.value.copy(error = it.msg.toString(), loading = false)
                    }

                    is ResultState.Loading -> {
                        _homeState.value = _homeState.value.copy(loading = true)
                    }
                }
            }
        }
    }

    fun toggleStore() {
        viewModelScope.launch {
            storeRepository.updateStore(homeState.value.store.copy(open = !homeState.value.store.open))
                .collect {
                    when (it) {
                        is ResultState.Success -> {
                            _homeState.value =
                                _homeState.value.copy(store = it.data, loading = false)
                        }

                        is ResultState.Failure -> {
                            _homeState.value =
                                _homeState.value.copy(error = it.msg.toString(), loading = false)
                        }

                        is ResultState.Loading -> {
                            _homeState.value = _homeState.value.copy(loading = true)
                        }
                    }
                }
        }
    }

    data class HomeState(
        val orders: List<Order> = emptyList(),
        val isUserLogged: Boolean = UserInfo.isUserLogged,
        val store: Store = Store(),
        val loading: Boolean = false,
        val error: String? = null
    )

    override fun onUserInfoChanged(isUserLogged: Boolean) {
        _homeState.value = _homeState.value.copy(isUserLogged = isUserLogged)
        if (isUserLogged) {
            getOrders()
            getStore()
        }
    }
}

package com.ipsoft.tocomsedeadmin.feature.account

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.PaymentMethod
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.Store
import com.ipsoft.tocomsede.core.utils.UserInfo
import com.ipsoft.tocomsedeadmin.data.store.RealtimeStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val storeRepository: RealtimeStoreRepository
) : ViewModel(), UserInfo.UserInfoListener {

    private
    val _storeState: MutableState<Store> = mutableStateOf(Store())
    val storeState: State<Store> = _storeState

    private val _isUserLogged: MutableState<Boolean> = mutableStateOf(UserInfo.isUserLogged)
    val isUserLogged: State<Boolean> = _isUserLogged

    override fun onUserInfoChanged(isUserLogged: Boolean) {
        _isUserLogged.value = isUserLogged
    }

    init {
        UserInfo.addListener(this)
        getStore()
    }

    private fun getStore() {
        viewModelScope.launch {
            storeRepository.getStore().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _storeState.value = result.data
                    }

                    is ResultState.Failure -> {
                    }

                    is ResultState.Loading -> {
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        UserInfo.removeListener(this)
    }

    fun togglePaymentMethod(payment: PaymentMethod) {
        val payments = _storeState.value.payments.toMutableList()
        if (payments.contains(payment)) {
            payments.remove(payment)
        } else {
            payments.add(payment)
        }

        _storeState.value.copy(payments = payments).let {
            viewModelScope.launch {
                storeRepository.updateStore(
                    it
                ).collect {
                    when (it) {
                        is ResultState.Success -> {
                            _storeState.value = it.data
                        }
                        is ResultState.Failure -> {
                        }
                        is ResultState.Loading -> {
                        }
                    }
                }
            }
        }
    }
}

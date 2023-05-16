package com.ipsoft.tocomsede.feature.phone

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.data.firebaserealtimedb.phone.RealtimePhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneViewModel @Inject constructor(private val phoneRepository: RealtimePhoneRepository) :
    ViewModel() {

    private val _phoneState = mutableStateOf(PhoneState())
    val phoneState: State<PhoneState> get() = _phoneState

    init {
        getPhone()
    }

    private fun getPhone() {
        viewModelScope.launch {
            phoneRepository.getPhone().collect {
                when (it) {
                    is ResultState.Success -> {
                        _phoneState.value = PhoneState(phone = it.data, loading = false)
                    }

                    is ResultState.Failure -> {
                        _phoneState.value = PhoneState(
                            phone = "",
                            loading = false,
                            error = it.msg.toString()
                        )
                    }

                    is ResultState.Loading -> {
                        _phoneState.value = PhoneState(phone = "", loading = true)
                    }
                }
            }
        }
    }

    fun savePhone(phone: String) {
        viewModelScope.launch {
            phoneRepository.savePhone(phone).collect {
                when (it) {
                    is ResultState.Success -> {
                        _phoneState.value =
                            PhoneState(phone = phone, loading = false, phoneUpdateSuccess = true)
                    }

                    is ResultState.Failure -> {
                        _phoneState.value = PhoneState(
                            phone = phone,
                            loading = false,
                            error = it.msg.toString()
                        )
                    }

                    is ResultState.Loading -> {
                        _phoneState.value = PhoneState(phone = phone, loading = true)
                    }
                }
            }
        }
    }

    data class PhoneState(
        val phone: String? = null,
        val loading: Boolean = false,
        val error: String = "",
        val phoneUpdateSuccess: Boolean = false
    )
}

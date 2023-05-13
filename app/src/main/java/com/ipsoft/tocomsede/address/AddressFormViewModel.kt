package com.ipsoft.tocomsede.address

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.CepResponse
import com.ipsoft.tocomsede.core.model.ResultState.Failure
import com.ipsoft.tocomsede.core.model.ResultState.Loading
import com.ipsoft.tocomsede.core.model.ResultState.Success
import com.ipsoft.tocomsede.data.cep.CepRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.address.RealtimeAddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressFormViewModel @Inject constructor(
    private val repository: CepRepository,
    private val addressRepository: RealtimeAddressRepository
) :
    ViewModel() {

    private val _cepState: MutableState<CepState> =
        mutableStateOf(CepState())
    val cepState: State<CepState> = _cepState
    fun getCep(cep: String) {
        viewModelScope.launch {
            repository.getCep(cep).collect {
                when (it) {
                    is Success -> {
                        it.data.let { cepResponse ->
                            _cepState.value = CepState(
                                cepResponse = cepResponse,
                                loading = false
                            )
                        }
                    }

                    is Failure -> {
                        _cepState.value = CepState(
                            cepResponse = null,
                            loading = false,
                            error = it.msg.toString()
                        )
                    }

                    is Loading -> {
                        _cepState.value = CepState(
                            cepResponse = null,
                            loading = true
                        )
                    }
                }
            }
        }
    }

    fun saveAddress(address: Address) {
        viewModelScope.launch {
            addressRepository.saveAddress(address).collect {
                when (it) {
                    is Success -> {
                        _cepState.value = _cepState.value.copy(
                            addressAddedSuccess = true
                        )
                    }

                    is Failure -> {
                        _cepState.value = CepState(
                            cepResponse = null,
                            loading = false,
                            error = it.msg.toString()
                        )
                    }

                    is Loading -> {
                        _cepState.value = CepState(
                            cepResponse = null,
                            loading = true
                        )
                    }
                }
            }
        }
    }

    data class CepState(
        val loading: Boolean = false,
        val cepResponse: CepResponse? = null,
        val error: String? = null,
        val addressAddedSuccess: Boolean = false
    )
}

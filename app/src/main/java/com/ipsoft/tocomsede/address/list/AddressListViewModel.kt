package com.ipsoft.tocomsede.address.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.ResultState.Failure
import com.ipsoft.tocomsede.core.model.ResultState.Loading
import com.ipsoft.tocomsede.core.model.ResultState.Success
import com.ipsoft.tocomsede.data.firebaserealtimedb.address.RealtimeAddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(private val addressRepository: RealtimeAddressRepository) :
    ViewModel() {

    private val _addressState: MutableState<AddressState> = mutableStateOf(AddressState())
    val addressState: State<AddressState> = _addressState

    init {
        getAddresses()
    }

    fun deleteAddress(address: Address) {
        viewModelScope.launch {
            addressRepository.deleteAddress(address).collect { deleteResponse ->
                when (deleteResponse) {
                    is Success -> {
                        getAddresses()
                    }

                    is Failure -> {
                        _addressState.value = _addressState.value.copy(
                            loading = false,
                            error = deleteResponse.msg.toString()
                        )
                    }

                    is Loading -> {
                        _addressState.value = AddressState(
                            addresses = null,
                            loading = true
                        )
                    }
                }
            }
        }
    }

    fun getAddresses() {
        viewModelScope.launch {
            addressRepository.getAddresses().collect {
                when (it) {
                    is Success -> {
                        it.data.let { addresses ->
                            _addressState.value = AddressState(
                                addresses = addresses,
                                loading = false
                            )
                        }
                    }

                    is Failure -> {
                        _addressState.value = AddressState(
                            addresses = null,
                            loading = false,
                            error = it.msg.toString()
                        )
                    }

                    is Loading -> {
                        _addressState.value = AddressState(
                            addresses = null,
                            loading = true
                        )
                    }
                }
            }
        }
    }

    data class AddressState(
        val addresses: List<Address>? = null,
        val loading: Boolean = false,
        val error: String? = null
    )
}

package com.ipsoft.tocomsede.itemdetails

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
import com.ipsoft.tocomsede.data.firebaserealtimedb.RealtimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(
    private val itemRepository: RealtimeRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _items: MutableState<ItemState> = mutableStateOf(ItemState())
    val items: State<ItemState> = _items

    private val _isSuccessFullCartAdded = mutableStateOf(false)
    val isSuccessFullCartAdded: State<Boolean> = _isSuccessFullCartAdded

    fun addItemToCart(item: Item, quantity: Int) {
        viewModelScope.launch {
            _isSuccessFullCartAdded.value = cartRepository.addItemToCart(item, quantity) is Success
        }
    }

    override fun onCleared() {
        super.onCleared()
        _items.value = ItemState()
    }

    fun getItemById(itemId: Int) {
        viewModelScope.launch {
            itemRepository.getItemById(itemId).collect {
                when (it) {
                    is Success -> {
                        _items.value = ItemState(
                            item = it.data,
                            isLoading = false
                        )
                    }

                    is Failure -> {
                        _items.value = ItemState(
                            error = it.msg.toString()
                        )
                    }

                    Loading -> {
                        _items.value = ItemState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun resetCartAddedStatus() {
        _isSuccessFullCartAdded.value = false
    }
}

data class ItemState(
    val item: Item? = null,
    val error: String? = null,
    val isLoading: Boolean = true
)

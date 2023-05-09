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
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _items: MutableState<ItemDetailScreenState> =
        mutableStateOf(ItemDetailScreenState())
    val items: State<ItemDetailScreenState> = _items

    private val _isSuccessFullCartAdded = mutableStateOf(false)
    val isSuccessFullCartAdded: State<Boolean> = _isSuccessFullCartAdded

    fun addItemToCart(item: Item, quantity: Int) {
        viewModelScope.launch {
            _isSuccessFullCartAdded.value = cartRepository.addItemToCart(item, quantity) is Success
        }
    }

    private fun checkIfItemIsInCartAndReturnQuantity(item: Item) {
        viewModelScope.launch {
            _items.value = _items.value.copy(
                quantityInCart = cartRepository.checkIfItemIsInCartAndReturnQuantity(item)
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        _items.value = ItemDetailScreenState()
    }

    fun getItemById(itemId: Int) {
        viewModelScope.launch {
            itemRepository.getItemById(itemId).collect {
                when (it) {
                    is Success -> {
                        it.data?.let { item ->
                            checkIfItemIsInCartAndReturnQuantity(item)
                        }
                        _items.value = ItemDetailScreenState(
                            item = it.data,
                            isLoading = false
                        )
                    }

                    is Failure -> {
                        _items.value = ItemDetailScreenState(
                            error = it.msg.toString()
                        )
                    }

                    Loading    -> {
                        _items.value = ItemDetailScreenState(
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

data class ItemDetailScreenState(
    val item: Item? = null,
    val error: String? = null,
    val isLoading: Boolean = true,
    val quantityInCart: Int = 0,
) {
    val canAddToCart: Boolean
        get() = item?.isAvailable == true && quantityInCart < item.quantity
}

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
import com.ipsoft.tocomsede.data.firebaserealtimedb.RealtimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(private val repo: RealtimeRepository) : ViewModel() {

    private val _items: MutableState<ItemState> = mutableStateOf(ItemState())
    val items: State<ItemState> = _items

    fun getItemById(itemId: Int) {
        viewModelScope.launch {
            repo.getItemById(itemId).collect {
                when (it) {
                    is Success -> {
                        _items.value = ItemState(
                            item = it.data
                        )
                    }

                    is Failure -> {
                        _items.value = ItemState(
                            error = it.msg.toString()
                        )
                    }

                    Loading    -> {
                        _items.value = ItemState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

}

data class ItemState(
    val item: Item? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
)
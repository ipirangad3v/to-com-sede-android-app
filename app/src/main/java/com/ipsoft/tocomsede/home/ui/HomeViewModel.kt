package com.ipsoft.tocomsede.home.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.data.firebaserealtimedb.RealtimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: RealtimeRepository) :
    ViewModel() {

    private val _items: MutableState<ItemsState> = mutableStateOf(ItemsState())
    val items: State<ItemsState> = _items

    fun loadItems() {
        viewModelScope.launch {
            repo.getItems().collect {
                when (it) {
                    is ResultState.Success -> {
                        _items.value = ItemsState(
                            item = it.data
                        )
                    }

                    is ResultState.Failure -> {
                        _items.value = ItemsState(
                            error = it.msg.toString()
                        )
                    }

                    ResultState.Loading -> {
                        _items.value = ItemsState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
    init {
        loadItems()
    }
}
data class ItemsState(
    val item: List<Item> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

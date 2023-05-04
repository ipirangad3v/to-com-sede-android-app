package com.ipsoft.tocomsede.home.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.data.firebaserealtimedb.RealtimeRepository
import com.ipsoft.tocomsede.utils.Info.isUserLogged
import com.ipsoft.tocomsede.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: RealtimeRepository) :
    ViewModel() {
    fun loadItems() {
        viewModelScope.launch {
            repo.getItems().collect {
                when (it) {
                    is ResultState.Success -> {
                        _items.value = ItemState(
                            item = it.data
                        )
                    }

                    is ResultState.Failure -> {
                        _items.value = ItemState(
                            error = it.msg.toString()
                        )
                    }

                    ResultState.Loading    -> {
                        _items.value = ItemState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    private val _items: MutableState<ItemState> = mutableStateOf(ItemState())
    val items: State<ItemState> = _items

    private val _userLogged
        get() = mutableStateOf(isUserLogged())
    val userLogged: State<Boolean> = _userLogged


    init {
        loadItems()
    }

}

data class ItemState(
    val item: List<Item> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false,
)
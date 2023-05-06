package com.ipsoft.tocomsede.home.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Category
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.data.firebaserealtimedb.RealtimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: RealtimeRepository) :
    ViewModel() {

    private val _categories: MutableState<CategoryState> = mutableStateOf(CategoryState())
    val categories: State<CategoryState> = _categories

    fun loadItems() {
        viewModelScope.launch {
            repo.getItems().collect {
                when (it) {
                    is ResultState.Success -> {
                        _categories.value = CategoryState(
                            item = it.data
                        )
                    }

                    is ResultState.Failure -> {
                        _categories.value = CategoryState(
                            error = it.msg.toString()
                        )
                    }

                    ResultState.Loading -> {
                        _categories.value = CategoryState(
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

data class CategoryState(
    val item: List<Category> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

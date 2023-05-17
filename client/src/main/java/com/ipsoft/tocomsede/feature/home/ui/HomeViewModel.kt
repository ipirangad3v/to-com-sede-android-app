package com.ipsoft.tocomsede.feature.home.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipsoft.tocomsede.core.model.Category
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.Store
import com.ipsoft.tocomsede.core.utils.UserInfo
import com.ipsoft.tocomsede.data.firebaserealtimedb.items.RealtimeItemRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.store.RealtimeStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val realtimeItemRepository: RealtimeItemRepository,
    private val storeRepository: RealtimeStoreRepository
) :
    ViewModel(), UserInfo.UserInfoListener {

    private val _store = mutableStateOf(Store())
    val store: State<Store> = _store

    private val _categories: MutableState<CategoryState> = mutableStateOf(CategoryState())
    val categories: State<CategoryState> = _categories

    private val _isUserLogged: MutableState<Boolean> = mutableStateOf(UserInfo.isUserLogged)
    val isUserLogged: State<Boolean> = _isUserLogged

    init {
        UserInfo.addListener(this)
        getStore()
    }

    private fun getStore() {
        viewModelScope.launch {
            storeRepository.getStore().collect {
                when (it) {
                    is ResultState.Success -> {
                        _store.value = it.data
                    }

                    is ResultState.Failure -> {
                        _store.value = Store()
                    }

                    ResultState.Loading -> {
                        _store.value = Store()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        UserInfo.removeListener(this)
    }

    fun loadItems() {
        viewModelScope.launch {
            realtimeItemRepository.getItems().collect {
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

    override fun onUserInfoChanged(isUserLogged: Boolean) {
        _isUserLogged.value = isUserLogged
    }
}

data class CategoryState(
    val item: List<Category> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

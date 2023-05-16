package com.ipsoft.tocomsede.feature.account

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ipsoft.tocomsede.utils.UserInfo
import javax.inject.Inject

class AccountViewModel @Inject constructor() : ViewModel(), UserInfo.UserInfoListener {

    private val _isUserLogged: MutableState<Boolean> = mutableStateOf(UserInfo.isUserLogged)
    val isUserLogged: State<Boolean> = _isUserLogged

    override fun onUserInfoChanged(isUserLogged: Boolean) {
        _isUserLogged.value = isUserLogged
    }

    init {
        UserInfo.addListener(this)
    }

    override fun onCleared() {
        super.onCleared()
        UserInfo.removeListener(this)
    }
}

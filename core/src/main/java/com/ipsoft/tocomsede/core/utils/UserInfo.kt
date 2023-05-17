package com.ipsoft.tocomsede.core.utils

import com.ipsoft.tocomsede.core.model.User

object UserInfo {

    private val listeners = mutableListOf<UserInfoListener>()
    var loggedUser: User? = null
        set(value) {
            field = value
            notifyListeners()
        }

    val isUserLogged: Boolean
        get() = loggedUser != null && loggedUser?.uid != null

    val userUid
        get() = if (isUserLogged) loggedUser?.uid else null

    fun addListener(listener: UserInfoListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: UserInfoListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.onUserInfoChanged(isUserLogged) }
    }

    interface UserInfoListener {
        fun onUserInfoChanged(isUserLogged: Boolean)
    }
}

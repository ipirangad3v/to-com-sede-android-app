package com.ipsoft.tocomsede.utils

import android.util.Log
import com.ipsoft.tocomsede.core.model.User

object UserInfo {

    private val listeners = mutableListOf<UserInfoListener>()
    var loggedUser: User? = null
        get() {
            Log.d("Info", "loggedUser: $field")
            return field
        }
        set(value) {
            field = value
            notifyListeners()
        }

    val isUserLogged: Boolean
        get() = loggedUser != null && loggedUser?.uid != null

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
        fun onUserInfoChanged(isUserLogged: Boolean = false)
    }
}

package com.ipsoft.tocomsede.utils

import android.util.Log
import com.ipsoft.tocomsede.core.model.User

object UserInfo {
    var loggedUser: User? = null
        get() {
            Log.d("Info", "loggedUser: $field")
            return field
        }

    fun isUserLogged() = loggedUser != null
}

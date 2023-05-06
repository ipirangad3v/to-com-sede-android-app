package com.ipsoft.tocomsede.core.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

fun Context.showMsg(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

val Context.connectivityManager: ConnectivityManager
    get() =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

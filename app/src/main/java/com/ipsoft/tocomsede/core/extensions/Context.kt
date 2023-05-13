package com.ipsoft.tocomsede.core.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

fun Context.showMsg(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.getVerCode(): String {
    return try {
        val pInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        pInfo.versionName
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

val Context.connectivityManager: ConnectivityManager
    get() =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

package com.ipsoft.tocomsede.core.extensions

import android.content.Context
import android.widget.Toast

fun Context.showMsg(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
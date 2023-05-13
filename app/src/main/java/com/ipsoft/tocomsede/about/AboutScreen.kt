package com.ipsoft.tocomsede.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AboutScreen(onAddressesClick: () -> Unit, onLoginClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column() {
            Text(text = "Sobre")
            Button(onClick = onAddressesClick) {
                Text(text = "Endereços")
            }
            Button(onClick = onLoginClick) {
                Text(text = "Login")
            }
        }
//        if (isUserLogged) {
//
//        }
    }
}

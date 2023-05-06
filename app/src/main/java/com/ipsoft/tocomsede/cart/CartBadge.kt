package com.ipsoft.tocomsede.cart

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBadge(cartItemsCount: Int, iconColor: Color) {
    BadgedBox(badge = { Badge { Text("$cartItemsCount") } }) {
        Icon(
            Icons.Filled.ShoppingCart,
            contentDescription = "cart",
            tint = iconColor
        )
    }

}
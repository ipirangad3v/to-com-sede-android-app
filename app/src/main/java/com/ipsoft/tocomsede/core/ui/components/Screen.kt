package com.ipsoft.tocomsede.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.ipsoft.tocomsede.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Filled.Info)
    object Cart : Screen("cart", R.string.cart, Icons.Filled.ShoppingCart)
    object Orders : Screen("orders", R.string.orders, Icons.Filled.Check)
    object Address : Screen("address", R.string.address, Icons.Filled.Build)
    object About : Screen("about", R.string.about, Icons.Filled.Info)

    companion object {
        val items = listOf(Home, Cart, Orders, Address, About)
    }
}
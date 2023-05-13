package com.ipsoft.tocomsede.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.ipsoft.tocomsede.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Home : Screen("home", R.string.home, Icons.Filled.Home)
    object Cart : Screen("cart", R.string.cart, Icons.Filled.ShoppingCart)
    object Orders : Screen("orders", R.string.orders, Icons.Filled.List)
    object About : Screen("about", R.string.about, Icons.Filled.Info)
    object ItemDetails :
        Screen("item_details/{$ITEM_ID}", R.string.item_details, Icons.Filled.ArrowBack)

    object Checkout : Screen("checkout", R.string.checkout, Icons.Filled.ShoppingCart)

    object AddressForm : Screen("address_form", R.string.address_form, Icons.Filled.Build)

    companion object {
        val items = listOf(Home, Cart, Orders, About)
        const val ITEM_ID = "itemId"
    }
}

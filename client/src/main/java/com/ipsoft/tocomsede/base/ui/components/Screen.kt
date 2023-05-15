package com.ipsoft.tocomsede.base.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.ipsoft.tocomsede.R

sealed class Screen(
    val route: String,
    @Suppress("unused")
    @StringRes
    val resourceId: Int,
    val icon: ImageVector
) {
    object Home : Screen("home", R.string.home, Icons.Filled.Home)
    object Cart : Screen("cart", R.string.cart, Icons.Filled.ShoppingCart)
    object Orders : Screen("orders", R.string.orders, Icons.Filled.List)
    object Account : Screen("about", R.string.account, Icons.Filled.Person)
    object ItemDetails :
        Screen("item_details/{$ITEM_ID}", R.string.item_details, Icons.Filled.ArrowBack)

    object AddressForm : Screen("address_form", R.string.address_form, Icons.Filled.Build)

    object AddressList : Screen("address_list", R.string.addresses, Icons.Filled.Add)

    object Phone : Screen("phone", R.string.phone, Icons.Filled.Add)

    companion object {
        val items = listOf(Home, Cart, Orders, Account)
        const val ITEM_ID = "itemId"
    }
}

package com.ipsoft.tocomsedeadmin.base.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.ipsoft.tocomsedeadmin.R

sealed class Screen(
    val route: String,
    @Suppress("unused")
    @StringRes
    val resourceId: Int,
    val icon: ImageVector
) {
    object Home : Screen("home", R.string.home, Icons.Filled.Home)
    object Account : Screen("about", R.string.account, Icons.Filled.Person)
    object OrderDetails : Screen("order_details/{$ORDER_ID}", R.string.order_details, Icons.Filled.Check)

    companion object {
        val items = listOf(Home, Account)
        const val ORDER_ID = "orderId"
    }
}

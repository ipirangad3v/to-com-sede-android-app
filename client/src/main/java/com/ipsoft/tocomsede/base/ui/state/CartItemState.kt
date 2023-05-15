package com.ipsoft.tocomsede.base.ui.state

import com.ipsoft.tocomsede.core.model.Item

data class CartItemState(
    val items: List<Item> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val checkoutSuccess: Boolean = false
)

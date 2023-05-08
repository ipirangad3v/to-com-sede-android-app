package com.ipsoft.tocomsede.core.ui.state

import com.ipsoft.tocomsede.core.model.Item

data class CartItemState(
    val items: List<Pair<Item, Int>> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

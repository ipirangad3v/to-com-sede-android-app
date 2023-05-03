package com.ipsoft.tocomsede.home.ui

import com.ipsoft.tocomsede.base.model.Item

data class HomeState(
    val isLoading: Boolean = false,
    val topItems: List<Item>? = null,
)
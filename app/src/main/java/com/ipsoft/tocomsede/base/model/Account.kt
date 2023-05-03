package com.ipsoft.tocomsede.base.model

data class Account(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
)
package com.ipsoft.tocomsede.base.model

data class Address(
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val street: String,
    val number: String,
    val complement: String,
    val neighborhood: String,
)
package com.ipsoft.tocomsede.core.model

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val photoUrl: String? = null,
    val uid: String
)

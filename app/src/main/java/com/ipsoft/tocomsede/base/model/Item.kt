package com.ipsoft.tocomsede.base.model

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val quantity: Int
){
    constructor() : this(0, "", "", 0.0, "", "", 0)
}

package com.works.final_exam.models

import java.io.Serializable

data class CartResponse(
    val carts: List<Cart>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class Cart(
    val id: Int,
    val products: List<CartItem>,
    val total: Int,
    val discountedTotal: Int,
    val userId: Int,
    val totalProducts: Int,
    val totalQuantity: Int
)

data class CartItem(
val id: Long,
val title: String,
val price: Long,
val quantity: Long,
val total: Long,
val discountPercentage: Double,
val discountedPrice: Long,
val thumbnail: String
): Serializable

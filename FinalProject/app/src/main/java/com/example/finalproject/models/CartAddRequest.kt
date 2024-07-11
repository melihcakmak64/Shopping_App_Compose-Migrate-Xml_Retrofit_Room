package com.example.finalproject.models

data class AddToCartRequest(
    val userId: Int,
    val products: List<AddToCartProductRequest>
)

data class AddToCartProductRequest(
    val id: Int,
    val quantity: Int
)

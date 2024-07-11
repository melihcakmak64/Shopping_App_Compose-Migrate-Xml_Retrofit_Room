package com.example.finalproject.configs

import com.example.finalproject.models.Product

interface ProductActions {
    fun addProduct(product: Product)
    fun deleteProduct(productId: Long)
    fun getProductbyId(productId: Long):Product?
}
// Update OrdersViewModel to include fetching the cart content
package com.example.finalproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.configs.ApiClient
import com.example.finalproject.models.Cart
import com.example.finalproject.models.CartProduct
import com.example.finalproject.services.DummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel : ViewModel() {

    private val _products = MutableLiveData<List<CartProduct>>()
    val products: LiveData<List<CartProduct>> get() = _products
    private val _total = MutableLiveData<Double>()
    val total: LiveData<Double> get() = _total

    private val _discountedTotal = MutableLiveData<Double>()
    val discountedTotal: LiveData<Double> get() = _discountedTotal

    private val dummyService: DummyService = ApiClient.getClient().create(DummyService::class.java)

    init {
        fetchCart()
    }

    private fun fetchCart() {
        val cartId = 6 // assuming we are fetching cart with id 1
        dummyService.getCart(cartId).enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful) {
                    response.body()?.let { cart ->
                        _products.value = cart.products
                        _total.value = cart.total
                        _discountedTotal.value = cart.discountedTotal
                        println(cart.total)
                        println(cart.discountedTotal)

                    }
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                // Handle the error as needed
            }
        })
    }
}

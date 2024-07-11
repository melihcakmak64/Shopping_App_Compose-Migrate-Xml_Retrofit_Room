package com.example.finalproject.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.models.Product
import com.example.finalproject.models.Products
import com.example.finalproject.services.DummyService
import com.example.finalproject.configs.ApiClient
import com.example.finalproject.models.AddToCartProductRequest
import com.example.finalproject.models.AddToCartRequest
import com.example.finalproject.models.Cart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val dummyService: DummyService = ApiClient.getClient().create(DummyService::class.java)



     fun searchProducts(query:String) {

            dummyService.searchProducts(query).enqueue(object : Callback<Products> {
                override fun onResponse(call: Call<Products>, response: Response<Products>) {
                    if (response.isSuccessful) {
                        _products.value = response.body()?.products ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<Products>, t: Throwable) {
                    // Handle the error as needed
                }
            })

    }

    fun addToCart(userId: Int, products: List<Product>, callback: (Boolean) -> Unit) {
        val productsToAdd = products.map { product ->
            AddToCartProductRequest(product.id.toInt(), 1) // Assuming quantity is always 1 for simplicity
        }
        dummyService.addToCart(AddToCartRequest(userId, productsToAdd))
            .enqueue(object : Callback<Cart> {
                override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                    if (response.isSuccessful) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<Cart>, t: Throwable) {
                    callback(false)
                }
            })
    }
}

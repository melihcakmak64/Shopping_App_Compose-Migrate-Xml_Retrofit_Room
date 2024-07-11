package com.example.finalproject.viewmodel
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.configs.ApiClient
import com.example.finalproject.configs.ProductActions
import com.example.finalproject.models.AddToCartProductRequest
import com.example.finalproject.models.AddToCartRequest
import com.example.finalproject.models.Cart
import com.example.finalproject.models.Product
import com.example.finalproject.services.DummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavoritesViewModel : ViewModel() {

    private val productService = ProductService.getInstance()
    private val dummyService: DummyService = ApiClient.getClient().create(DummyService::class.java)


    // LiveData for products
    private val _productsLiveData = productService.productsLiveData
    val products: LiveData<List<Product>> get() = _productsLiveData

    // Function to delete a product
    fun deleteProduct(productId: Long) {
        productService.deleteProduct(productId)
    }

    // Function to add a product
    fun addProduct(product: Product) {
        productService.addProduct(product)
    }

    // Function to update a product
    fun updateProduct(product: Product) {
        productService.updateProduct(product)
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






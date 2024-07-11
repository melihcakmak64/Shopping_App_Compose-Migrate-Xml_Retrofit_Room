package com.example.finalproject.services

import com.example.finalproject.models.AddToCartRequest
import com.example.finalproject.models.Cart
import com.example.finalproject.models.Category
import com.example.finalproject.models.LoginRequest
import com.example.finalproject.models.LoginResponse
import com.example.finalproject.models.Products
import com.example.finalproject.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyService {

    @GET("products")
    fun getProducts(): Call<Products>

    @GET("products/search")
    fun searchProducts(@Query("q") query: String): Call<Products>

    @GET("products/categories")
    fun getCategories(): Call<List<Category>>
    @GET("products/category/{slug}")
    fun getProductsByCategory(@Path("slug") categorySlug: String): Call<Products>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): User

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") userId: Int, @Body user: User): User

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("carts/{id}")
    fun getCart(@Path("id") cartId: Int): Call<Cart>

    @POST("carts/add")
    fun addToCart(@Body request: AddToCartRequest): Call<Cart>




}

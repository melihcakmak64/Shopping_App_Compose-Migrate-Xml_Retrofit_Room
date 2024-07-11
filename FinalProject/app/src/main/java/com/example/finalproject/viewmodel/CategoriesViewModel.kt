package com.example.finalproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.configs.ApiClient
import com.example.finalproject.models.Category
import com.example.finalproject.services.DummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesViewModel: ViewModel() {
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val dummyService: DummyService = ApiClient.getClient().create(DummyService::class.java)

    init {
        fetchCategories()
        println(categories.value)
    }

    private fun fetchCategories() {
        if (_categories.value.isNullOrEmpty()) {
            dummyService.getCategories().enqueue(object : Callback<List<Category>> {
                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                    if (response.isSuccessful) {
                        println("başarılııı")
                        _categories.value = response.body() ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    // Handle the error as needed
                    println(t.message)
                }
            })
        }
    }
}

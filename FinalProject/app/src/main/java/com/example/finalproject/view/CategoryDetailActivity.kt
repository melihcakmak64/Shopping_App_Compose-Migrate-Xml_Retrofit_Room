package com.example.finalproject.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.finalproject.R
import com.example.finalproject.adaptors.ProductAdaptors
import com.example.finalproject.databinding.ActivityCategoryDetailBinding
import com.example.finalproject.databinding.ActivityMainBinding
import com.example.finalproject.viewmodel.CategoryDetailActivityViewModel

class CategoryDetailActivity : AppCompatActivity() {
    private val viewModel: CategoryDetailActivityViewModel by viewModels()
    private lateinit var productAdaptors: ProductAdaptors
    private lateinit var binding: ActivityCategoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productAdaptors = ProductAdaptors(this, mutableListOf()){ clickedProduct ->
            // Tıklanan ürünü alıp addToCart metoduna gönderiyoruz
            val userId = 1 // Replace with actual userId
            viewModel.addToCart(userId, listOf(clickedProduct)) { success ->
                if (success) {
                    showToast("Product added to cart successfully.")
                } else {
                    showToast("Failed to add product to cart.")
                }
            }
        }
        binding.listViewProduct.adapter=productAdaptors

        val categorySlug = intent.getStringExtra("CATEGORY_SLUG") ?: return

        viewModel.fetchProductsByCategory(categorySlug)

        viewModel.products.observe(this, Observer { products ->
            productAdaptors.updateData(products)

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
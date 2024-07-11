package com.example.finalproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.example.finalproject.adaptors.ProductAdaptors
import com.example.finalproject.databinding.FragmentSearchBinding

import com.example.finalproject.viewmodel.SearchViewModel


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdaptors: ProductAdaptors
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        productAdaptors = ProductAdaptors(requireContext(), mutableListOf()) { clickedProduct ->
            // Tıklanan ürünü alıp addToCart metoduna gönderiyoruz
            val userId = 1 // Replace with actual userId
            searchViewModel.addToCart(userId, listOf(clickedProduct)) { success ->
                if (success) {
                    showToast("Product added to cart successfully.")
                } else {
                    showToast("Failed to add product to cart.")
                }
            }
        }
        binding.listViewSearch.adapter = productAdaptors

        searchViewModel.products.observe(viewLifecycleOwner, Observer { newProducts ->
            productAdaptors.clear()
            productAdaptors.updateData(newProducts)
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchViewModel.searchProducts(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
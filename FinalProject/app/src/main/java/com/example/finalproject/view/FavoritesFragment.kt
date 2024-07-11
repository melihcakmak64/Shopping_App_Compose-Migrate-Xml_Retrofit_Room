package com.example.finalproject.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.adaptors.ProductAdaptors
import com.example.finalproject.databinding.FragmentFavoritesBinding
import com.example.finalproject.viewmodel.FavoritesViewModel



class FavoritesFragment : Fragment() {
    private var _binding:FragmentFavoritesBinding?=null
    private val binding get() = _binding!!
    private lateinit var productAdaptors: ProductAdaptors
    private val favoritesViewModel:FavoritesViewModel by viewModels()






    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)





    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        productAdaptors = ProductAdaptors(requireContext(), mutableListOf()){ clickedProduct ->
            // Tıklanan ürünü alıp addToCart metoduna gönderiyoruz
            val userId = 1 // Replace with actual userId
            favoritesViewModel.addToCart(userId, listOf(clickedProduct)) { success ->
                if (success) {
                    showToast("Product added to cart successfully.")
                } else {
                    showToast("Failed to add product to cart.")
                }
            }
        }
        binding.listViewFavorites.adapter = productAdaptors

        favoritesViewModel.products.observe(viewLifecycleOwner, Observer { newProducts ->
            productAdaptors.updateData(newProducts)
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
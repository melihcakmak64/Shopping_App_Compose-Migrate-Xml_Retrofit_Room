package com.example.finalproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.finalproject.adaptors.ProductAdaptors
import com.example.finalproject.configs.LocalStorage
import com.example.finalproject.databinding.FragmentHomeBinding
import com.example.finalproject.models.Product
import com.example.finalproject.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdaptors: ProductAdaptors
    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        val localStorage = LocalStorage(requireContext())
        val user = localStorage.getUser()




        productAdaptors = ProductAdaptors(requireContext(), mutableListOf()) { clickedProduct ->

            homeViewModel.addToCart(user!!.id, listOf(clickedProduct)) { success ->
                if (success) {
                    showToast("Product added to cart successfully.")
                } else {
                    showToast("Failed to add product to cart.")
                }
            }
        }
        binding.listViewProducts.adapter = productAdaptors


        homeViewModel.products.observe(viewLifecycleOwner, Observer { newProducts ->
            productAdaptors.updateData(newProducts)
        })



        return view
    }



    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

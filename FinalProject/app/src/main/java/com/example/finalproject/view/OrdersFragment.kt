package com.example.finalproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.finalproject.R
import com.example.finalproject.adaptors.OrdersAdaptors
import com.example.finalproject.adaptors.ProductAdaptors
import com.example.finalproject.databinding.FragmentHomeBinding
import com.example.finalproject.databinding.FragmentOrdersBinding
import com.example.finalproject.viewmodel.OrdersViewModel


class OrdersFragment : Fragment() {

   private var _binding:FragmentOrdersBinding?=null
    private val binding get() = _binding!!
    private lateinit var ordersAdaptors: OrdersAdaptors
    private val ordersViewModel:OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val view = binding.root
        ordersAdaptors = OrdersAdaptors(requireContext(), mutableListOf())
        binding.listViewOrders.adapter = ordersAdaptors



        ordersViewModel.products.observe(viewLifecycleOwner, Observer { newProducts ->
            ordersAdaptors.updateData(newProducts)
            binding.txtTotal.text=ordersViewModel.total.value.toString()
            binding.txtDiscountedTotal.text=ordersViewModel.discountedTotal.value.toString()
        })


        ordersViewModel.total.observe(viewLifecycleOwner, Observer { total ->
            binding.txtTotal.text = "Total: "+total?.toString() ?: "0.0"
        })


        ordersViewModel.discountedTotal.observe(viewLifecycleOwner, Observer { discountedTotal ->
            binding.txtDiscountedTotal.text = "Discounted Total: "+discountedTotal?.toString() ?: "0.0"
        })




        return view
    }


}
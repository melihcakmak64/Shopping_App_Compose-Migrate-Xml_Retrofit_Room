package com.example.finalproject.view
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.finalproject.adaptors.CategoryAdapter
import com.example.finalproject.adaptors.ProductAdaptors
import com.example.finalproject.databinding.FragmentCategoriesBinding
import com.example.finalproject.viewmodel.CategoriesViewModel


class CategoriesFragment : Fragment() {
    private var _binding:FragmentCategoriesBinding?=null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryAdapter

    private val categoriesViewModel: CategoriesViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        categoryAdapter = CategoryAdapter(requireContext(), mutableListOf())
        binding.listViewCategories.adapter=categoryAdapter

        binding.listViewCategories.setOnItemClickListener { parent, view, position, id ->
            val selectedCategory = categoryAdapter.getItem(position)
            val intent = Intent(requireContext(), CategoryDetailActivity::class.java).apply {
                putExtra("CATEGORY_SLUG", selectedCategory?.slug)
            }
            startActivity(intent)
        }
        categoriesViewModel.categories.observe(viewLifecycleOwner, Observer { categories ->
            categoryAdapter.updateData(categories)
        })

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
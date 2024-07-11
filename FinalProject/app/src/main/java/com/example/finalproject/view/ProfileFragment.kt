package com.example.finalproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.finalproject.databinding.FragmentProfileBinding
import com.example.finalproject.models.User
import com.example.finalproject.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchUser(1) // Fetching user with id 5
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.user.observe(viewLifecycleOwner) { user ->
            Glide.with(this).load(user.image).into(binding.profileImage)
            binding.editFirstName.setText(user.firstName)
            binding.editLastName.setText(user.lastName)
            binding.editMaidenName.setText(user.maidenName)
            binding.editAge.setText(user.age.toString())
            binding.editGender.setText(user.gender)
            binding.editEmail.setText(user.email)
            binding.editPhone.setText(user.phone)
        }

        binding.buttonSave.setOnClickListener {
            val updatedUser = User(
                id = 1,
                firstName = binding.editFirstName.text.toString(),
                lastName =  binding.editLastName.text.toString(),
                maidenName = binding.editMaidenName.text.toString(),
                age = binding.editAge.text.toString().toInt(),
                gender = binding.editGender.text.toString(),
                email = binding.editEmail.text.toString(),
                phone = binding.editPhone.text.toString(),
                username = "emmaj",
                password = "emmajpass",
                birthDate = "1994-6-13",
                image = "https://dummyjson.com/icon/emmaj/128"
            )
            viewModel.updateUser(5, updatedUser)
        }
        viewModel.updateResult.observe(viewLifecycleOwner) { result ->
            if (result) {
                showToast("Profile updated successfully")
            } else {
                showToast("Failed to update profile")
            }
        }

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

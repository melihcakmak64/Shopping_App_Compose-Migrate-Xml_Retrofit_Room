package com.example.finalproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.configs.ApiClient
import com.example.finalproject.models.User
import com.example.finalproject.services.DummyService
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    private val _updateResult = MutableLiveData<Boolean>() // LiveData for update result
    private val dummyService: DummyService = ApiClient.getClient().create(DummyService::class.java)
    val user: LiveData<User> get() = _user
    val updateResult: LiveData<Boolean> get() = _updateResult // Expose update result as LiveData

    fun fetchUser(userId: Int) {
        viewModelScope.launch {
            val response = dummyService.getUser(userId)
            _user.postValue(response)
        }
    }

    fun updateUser(userId: Int, updatedUser: User) {
        viewModelScope.launch {
            val response = dummyService.updateUser(userId, updatedUser)
            _updateResult.postValue(response != null) // Assuming response is not null if update is successful
        }
    }
}

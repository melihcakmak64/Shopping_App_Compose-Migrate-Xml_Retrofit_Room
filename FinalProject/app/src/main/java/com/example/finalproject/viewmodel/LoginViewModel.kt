package com.example.finalproject.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.finalproject.configs.ApiClient
import com.example.finalproject.configs.LocalStorage
import com.example.finalproject.models.LoginRequest
import com.example.finalproject.models.User
import com.example.finalproject.services.DummyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val localStorage: LocalStorage) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var loginError by mutableStateOf(false)
    var isPasswordVisible by mutableStateOf(false)

    fun login(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            val retrofit = ApiClient.getClient()
            val service = retrofit.create(DummyService::class.java)
            val loginRequest = LoginRequest(username, password)

            try {
                val response = withContext(Dispatchers.IO) {
                    service.login(loginRequest).execute()
                }

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val user = User(
                            id = loginResponse.id,
                            firstName = loginResponse.firstName,
                            lastName = loginResponse.lastName,
                            maidenName = "", // Set default values for fields not in response
                            age = 0,        // Set default values for fields not in response
                            gender = loginResponse.gender,
                            email = loginResponse.email,
                            phone = "",     // Set default values for fields not in response
                            username = loginResponse.username,
                            password = "",  // Password should not be stored
                            birthDate = "", // Set default values for fields not in response
                            image = loginResponse.image
                        )
                        localStorage.setUser(user)
                        localStorage.setToken(loginResponse.token)
                        onLoginSuccess()
                    }
                } else {
                    loginError = true
                }
            } catch (e: Exception) {
                loginError = true
            }
        }
    }
}


class LoginViewModelFactory(private val localStorage: LocalStorage) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(localStorage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

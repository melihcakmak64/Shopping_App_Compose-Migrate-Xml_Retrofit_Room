package com.example.finalproject.configs
import android.content.Context
import com.example.finalproject.models.User
import com.google.gson.Gson

class LocalStorage(val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val gson = Gson()

    fun getUser(): User? {
        val jsonUser = sharedPreferences.getString("user", null)
        return if (jsonUser == null) null else gson.fromJson(jsonUser, User::class.java)
    }

    fun setUser(user: User) {
        val jsonUser = gson.toJson(user)
        editor.putString("user", jsonUser)
        editor.commit()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun setToken(token: String) {
        editor.putString("token", token)
        editor.commit()
    }

    fun clearUser() {
        editor.remove("user")
        editor.remove("token")
        editor.commit()
    }
}

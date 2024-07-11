package com.example.finalproject.view

import LoginScreen
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import com.example.finalproject.configs.LocalStorage
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {
    private lateinit var composeView: ComposeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val localStorage = LocalStorage(this)
        val token = localStorage.getToken()

        if (token != null) {
            // Token exists, user is already logged in
            navigateToMain()
            return
        }

        // Create a ComposeView and set it as the content view
        composeView = ComposeView(this)
        setContentView(composeView)

        // Set the content for the ComposeView
        composeView.setContent {
            LoginScreen(onPressed = {
                navigateToMain()
            })
        }

        requestPermission()
    }

    private fun requestPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, now get FCM token
                getFCMToken()
            } else {
                // Permission denied, handle accordingly
                Log.e("LoginActivity", "Permission denied for POST_NOTIFICATIONS")
                // You may inform the user or take alternative action here
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted, proceed to get FCM token
                getFCMToken()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // Explain to the user why the permission is needed, if needed
            } else {
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get new FCM registration token
                    val token = task.result
                    Log.d("LoginActivity", "FCM Token: $token")
                    // Store token if necessary or use it as required
                } else {
                    Log.e("LoginActivity", "Fetching FCM registration token failed", task.exception)
                    // Handle token generation failure
                }
            })
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

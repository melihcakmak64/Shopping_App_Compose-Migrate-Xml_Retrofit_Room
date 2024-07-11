package com.example.finalproject.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.configs.LocalStorage
import com.example.finalproject.databinding.ActivityMainBinding
import com.example.finalproject.models.Product

import com.google.android.material.navigation.NavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val localStorage = LocalStorage(this)
        val user=localStorage.getUser()

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Default değerleri ayarlayın
        remoteConfig.setDefaultsAsync(mapOf("background_color" to "#FFFFFF"))

        // Remote Config verilerini fetch edin
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val backgroundColor = remoteConfig.getString("background_color")
                    Log.d("MainActivity", "Remote Config fetch successful: $backgroundColor")
                    try {
                        binding.drawerLayout.setBackgroundColor(Color.parseColor(backgroundColor))
                        Log.d("MainActivity", "Background color set to: $backgroundColor")
                    } catch (e: IllegalArgumentException) {
                        Log.e("MainActivity", "Invalid color format: $backgroundColor")
                    }
                } else {
                    Log.e("MainActivity", "Remote Config fetch failed")
                }
            }

        val headerView = binding.navView.getHeaderView(0)
        val headerImage: ImageView = headerView.findViewById(R.id.headerImage)
        val headerName: TextView = headerView.findViewById(R.id.headerName)
        val headerMail: TextView = headerView.findViewById(R.id.headerMail)
        Glide.with(this)
            .load(user!!.image) // replace with the actual URL field in your User model
            .placeholder(R.drawable.logo) // replace with your placeholder
            .error(R.drawable.ic_launcher_background) // replace with your error drawable
            .into(headerImage)

        headerName.text = user!!.username
        headerMail.text = user!!.email














        setSupportActionBar(binding.toolbar)
        binding.navView.setNavigationItemSelectedListener(this)
        val toggle= ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                HomeFragment()
            ).commit()
            binding.navView.setCheckedItem(R.id.nav_home)
           updateActionBarTitle("Home")

        }

        ProductService.initialize(this)












    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> navigateToFragment(HomeFragment(), "Home")
            R.id.nav_categories -> navigateToFragment(CategoriesFragment(), "Categories")
            R.id.nav_search -> navigateToFragment(SearchFragment(), "Search")
            R.id.nav_favorites -> navigateToFragment(FavoritesFragment(), "Favorites")
            R.id.nav_orders -> navigateToFragment(OrdersFragment(), "Orders")
            R.id.nav_profile -> navigateToFragment(ProfileFragment(), "Profile")
            R.id.nav_logout -> {
                Toast.makeText(this, "You are leaving..", Toast.LENGTH_LONG).show()
                val localStorage = LocalStorage(this)
                localStorage.clearUser()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return  true
    }

    private fun navigateToFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        updateActionBarTitle(title)
    }
    private fun updateActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            onBackPressedDispatcher.onBackPressed()
        }
    }



}
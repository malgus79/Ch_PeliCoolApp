package com.moviemain.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.moviemain.R
import com.moviemain.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController)

        binding.bottomNavigationView.setupWithNavController(navController)

//        NavigationBarView.OnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.nav_home -> {
//                    true
//                }
//                R.id.nav_upcoming -> {
//                    true
//                }
//                R.id.nav_bookmark -> {
//                    true
//                }
//                R.id.nav_searh -> {
//                    true
//                }
//                else -> false
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.popBackStack()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.button_menu, menu)
        return true
    }
}
package com.moviemain.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.moviemain.R
import com.moviemain.core.utils.hide
import com.moviemain.core.utils.show
import com.moviemain.core.utils.showConnectivitySnackbar
import com.moviemain.databinding.ActivityMainBinding
import com.moviemain.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private var doubleBackToExitPressedOnce = false

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        checkNetworkConnection()

    }

    private fun setupNavigation() {
        val navView = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        NavigationUI.setupWithNavController(navView, navController)
        //NavigationUI.setupActionBarWithNavController(this, navController)

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.movieDetailFragment || nd.id == R.id.similarDetailFragment) {
                navView.hide()
            } else {
                navView.show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkNetworkConnection() {
        lifecycleScope.launchWhenCreated {
            viewModel.isConnected
                .distinctUntilChanged()
                .drop(1)
                .collect { isConnected ->
                    when (isConnected) {
                        true -> showConnectivitySnackbar(true)
                        false -> showConnectivitySnackbar(false)
                    }
                }
        }
    }

    fun exitApp(item: MenuItem) {
        when (item.itemId) {
            R.id.menu_exit -> finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            onBackPressedDispatcher.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(
                0
            ) is HomeFragment
        ) {
            Snackbar.make(binding.root, R.string.press_again, Snackbar.LENGTH_SHORT)
                .setAnchorView(binding.bottomNavigationView)
                .setTextColor(ContextCompat.getColor(this, R.color.black))
                .setBackgroundTint(ContextCompat.getColor(this, R.color.yellow))
                .show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            doubleBackToExitPressedOnce = false
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
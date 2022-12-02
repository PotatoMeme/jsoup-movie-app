package com.potatomeme.jsoupmovieapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.data.db.MovieDataBase
import com.potatomeme.jsoupmovieapp.data.repository.MovieRepositoryImpl
import com.potatomeme.jsoupmovieapp.databinding.ActivityMainBinding
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val database =MovieDataBase.getInstance(this)
        val movieRepository = MovieRepositoryImpl(database)
        val factory = MainViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupJetpackNavigation()
        binding.lifecycleOwner = this
    }

    private fun setupJetpackNavigation() {
        val host = supportFragmentManager
            .findFragmentById(R.id.movie_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_start
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
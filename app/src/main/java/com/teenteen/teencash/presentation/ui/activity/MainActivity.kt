package com.teenteen.teencash.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.teenteen.teencash.R
import com.teenteen.teencash.data.local.PrefsSettings
import com.teenteen.teencash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var pref: PrefsSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TeenCash)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        pref = PrefsSettings(this)

        when(pref.isFirstTimeLaunch()) {
            PrefsSettings.FIRST_TIME -> {
                navGraph.setStartDestination(R.id.onBoardingFlowFragment)
            }
            PrefsSettings.NOT_AUTH -> {
                navGraph.setStartDestination(R.id.signFlowFragment)
            }
            PrefsSettings.USER -> {
                navGraph.setStartDestination(R.id.mainFlowFragment)
            }
        }
        navController.graph = navGraph
    }
}
package com.teenteen.teencash.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.teenteen.teencash.R
import com.teenteen.teencash.data.local.PrefsSettings
import com.teenteen.teencash.databinding.ActivityMainBinding
import com.teenteen.teencash.presentation.extensions.updateLanguage
import com.teenteen.teencash.presentation.utills.checkInternetConnection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var prefs: PrefsSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TeenCash)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PrefsSettings(this)
        checkInternetConnection(this::setupNavigation , this , this::noConnection)
        getDeviceThemeMode()
        PreferenceManager(this).updateLanguage(prefs.getSettingsLanguage() , this , prefs)
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        when (prefs.isFirstTimeLaunch()) {
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

    private fun noConnection() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.setStartDestination(R.id.noConnectionFlowFragment)
        navController.graph = navGraph
    }

    private fun getDeviceThemeMode() {
        if (prefs.isFirstTimeLaunch() == PrefsSettings.FIRST_TIME) AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
        else if (prefs.getDarkThemeMode()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else if (! prefs.getDarkThemeMode()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
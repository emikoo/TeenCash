package com.teenteen.teencash.presentation.ui.fragments.main.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.teenteen.teencash.R
import com.teenteen.teencash.databinding.FragmentSettingsBinding
import com.teenteen.teencash.presentation.base.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun attachBinding(
        list: MutableList<FragmentSettingsBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentSettingsBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        getDeviceThemeMode()
        switchDeviceThemeMode()
    }

    private fun getDeviceThemeMode() {
        binding.switchMode.isChecked = prefs.getDarkThemeMode()
        val switchState = binding.switchMode.isChecked
        if (switchState){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.ivMode.setBackgroundResource(R.drawable.ic_dark_mode)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.ivMode.setBackgroundResource(R.drawable.ic_light_mode)
        }
    }

    private fun switchDeviceThemeMode() {
        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) prefs.saveDarkThemeMode(true)
            else prefs.saveDarkThemeMode(false)
            getDeviceThemeMode()
        }
    }

    override fun subscribeToLiveData() {}
}
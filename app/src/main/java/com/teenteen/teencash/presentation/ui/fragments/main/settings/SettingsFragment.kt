package com.teenteen.teencash.presentation.ui.fragments.main.settings

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.teenteen.teencash.R
import com.teenteen.teencash.data.local.PrefsSettings
import com.teenteen.teencash.databinding.FragmentSettingsBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.extensions.isVisible
import com.teenteen.teencash.presentation.extensions.show
import com.teenteen.teencash.presentation.interfaces.UpdateLanguage
import com.teenteen.teencash.presentation.ui.activity.MainActivity
import com.teenteen.teencash.presentation.ui.common_bottom_sheets.BottomSheetList
import com.teenteen.teencash.presentation.ui.fragments.main.settings.bottom_sheets.AskQuestionBottomSheet
import com.teenteen.teencash.presentation.utills.ListBottomSheetKeys

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() , UpdateLanguage {

    override fun attachBinding(
        list: MutableList<FragmentSettingsBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentSettingsBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        binding.btnSignOut.isVisible()
        getDeviceThemeMode()
        switchDeviceThemeMode()
        setupListener()
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
        binding.switchMode.setOnCheckedChangeListener { _ , isChecked ->
            if (isChecked) prefs.saveDarkThemeMode(true)
            else prefs.saveDarkThemeMode(false)
            getDeviceThemeMode()
        }
    }

    private fun setupListener() {
        binding.btnLang.setOnClickListener {
            BottomSheetList(
                ListBottomSheetKeys.CHANGE_LANGUAGE ,
                updateLang = this
            ).show(activity?.supportFragmentManager)
        }
        binding.btnCurrency.setOnClickListener {
            BottomSheetList(
                ListBottomSheetKeys.CHANGE_CURRENCY ,
                updateLang = this
            ).show(activity?.supportFragmentManager)
        }
        binding.btnAchievements.setOnClickListener {
            val directions =
                SettingsFragmentDirections.actionSettingsFragmentToAchievementsFragment()
            findNavController().navigate(directions)
        }
        binding.btnAsk.setOnClickListener {
            AskQuestionBottomSheet().show(activity?.supportFragmentManager)
        }

        binding.btnSignOut.setOnClickListener {
            prefs.saveCurrentUserId("")
            prefs.saveSettingsCurrency("KGS")
            prefs.setFirstTimeLaunch(PrefsSettings.NOT_AUTH)
            val intent = Intent(requireContext() , MainActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.pop_in , R.anim.pop_out)
            requireActivity().finishAffinity()
        }
    }

    override fun subscribeToLiveData() {}
    override fun updateLanguage() {
        val directions =
            SettingsFragmentDirections.actionSettingsFragmentSelf()
        findNavController().navigate(directions)
    }
}
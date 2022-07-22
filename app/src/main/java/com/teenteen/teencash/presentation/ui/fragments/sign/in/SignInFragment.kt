package com.teenteen.teencash.presentation.ui.fragments.sign.`in`

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.singleactivity.activityNavController
import com.example.singleactivity.navigateSafely
import com.teenteen.teencash.R
import com.teenteen.teencash.data.local.PrefsSettings
import com.teenteen.teencash.databinding.FragmentSignInBinding

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private lateinit var prefs: PrefsSettings

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        prefs = PrefsSettings(requireActivity())
        setupListeners()
    }

    private fun setupListeners() {
        clickSignIn()
        clickSignUp()
    }

    private fun clickSignIn() {
        binding.buttonSignIn.setOnClickListener {
            prefs.setFirstTimeLaunch(PrefsSettings.USER)
            activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
        }
    }

    private fun clickSignUp() {
        binding.buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }
}
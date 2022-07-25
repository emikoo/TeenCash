package com.teenteen.teencash.presentation.ui.fragments.sign

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.singleactivity.activityNavController
import com.example.singleactivity.navigateSafely
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.teenteen.teencash.R
import com.teenteen.teencash.data.local.PrefsSettings
import com.teenteen.teencash.databinding.FragmentAuthBinding

class AuthFragment: Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)
    private lateinit var prefs: PrefsSettings
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        prefs = PrefsSettings(requireActivity())
        setupListeners()
        setupTextWatcher(binding.inputEditEmail, binding.inputEditPassword)
        setupTextWatcher(binding.inputEditPassword, binding.inputEditEmail)
    }

    private fun setupListeners() {
        clickSignIn()
        clickSignUp()
    }

    private fun clickSignIn() {
        binding.buttonSignIn.setOnClickListener {
            if (checkField(binding.inputEditEmail, binding.inputEditPassword)) loginUser()
        }
    }

    private fun loginUser() {
        auth.signInWithEmailAndPassword(
            binding.inputEditEmail.text.toString() ,
            binding.inputEditPassword.text.toString()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (checkIfEmailVerified()) {
                        prefs.setFirstTimeLaunch(PrefsSettings.USER)
                        activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                    } else {
                        Toast.makeText(
                            getApplicationContext() ,
                            "Pls verify" ,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(
                        getApplicationContext() ,
                        "Login failed!!" ,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }

    private fun clickSignUp() {
        binding.buttonSignUp.setOnClickListener {
            if (checkField(binding.inputEditEmail, binding.inputEditPassword)) createNewUser()
        }
    }

    private fun createNewUser() {
        auth
            .createUserWithEmailAndPassword(
                binding.inputEditEmail.text.toString() ,
                binding.inputEditPassword.text.toString()
            )
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    sendVerificationEmail()
                } else {
                    Toast.makeText(
                        getApplicationContext() , "Registration failed!!"
                                + " Please try again later" ,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }

    private fun sendVerificationEmail() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        firebaseUser?.sendEmailVerification()
            ?.addOnSuccessListener {
                Toast.makeText(
                    getApplicationContext() ,
                    "Instructions Sent..." ,
                    Toast.LENGTH_SHORT
                ).show()
            }
            ?.addOnFailureListener { e ->
                Toast.makeText(
                    getApplicationContext() ,
                    "Failed to send due to " + e.message ,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun checkIfEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
    }

    private fun checkField(et1: EditText, et2: EditText): Boolean {
        if(et1.text.toString().isNullOrBlank() && et2.text.toString().isNullOrBlank()) {
            et1.background = resources.getDrawable(R.drawable.bg_field_red)
            et2.background = resources.getDrawable(R.drawable.bg_field_red)
            et1.error = getString(R.string.fill_in_the_field)
            et2.setError(getString(R.string.fill_in_the_field), null)
            return false
        } else if(et1.text.toString().isNullOrBlank()) {
            et1.background = resources.getDrawable(R.drawable.bg_field_red)
            et1.error = getString(R.string.fill_in_the_field)
            return false
        } else if (et2.text.toString().isNullOrBlank()) {
            et2.background = resources.getDrawable(R.drawable.bg_field_red)
            et2.setError(getString(R.string.fill_in_the_field), null)
            return false
        }
        return true
    }

    private fun setupTextWatcher(et1: EditText, et2: EditText) {
        et1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                et1.background = context?.resources?.getDrawable(R.drawable.bg_field_gray)
                et2.background = context?.resources?.getDrawable(R.drawable.bg_field_gray)
                et1.error = null
                et2.error = null
            }
        })
    }
}
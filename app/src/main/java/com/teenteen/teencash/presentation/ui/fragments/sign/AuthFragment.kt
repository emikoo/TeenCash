package com.teenteen.teencash.presentation.ui.fragments.sign

import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.teenteen.teencash.R
import com.teenteen.teencash.data.local.PrefsSettings.Companion.USER
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.FragmentAuthBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.extensions.*
import com.teenteen.teencash.presentation.utills.checkInternetConnection
import com.teenteen.teencash.presentation.utills.showAlertDialog
import com.teenteen.teencash.view_model.AuthViewModel

class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    lateinit var viewModel: AuthViewModel

    override fun attachBinding(
        list: MutableList<FragmentAuthBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentAuthBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        checkInternetConnection(this::setupListeners, requireContext())
        setupTextWatcher(binding.inputEditEmail , binding.inputEditPassword)
        setupTextWatcher(binding.inputEditPassword , binding.inputEditEmail)
    }

    private fun setupListeners() {
        clickSignIn()
        clickSignUp()
        clickForgotPassword()
    }

    private fun clickSignIn() {
        binding.buttonSignIn.setOnClickListener {
            if (checkField(binding.inputEditEmail , binding.inputEditPassword)){
                checkInternetConnection(this::loginUser, requireContext(), this::showNoInternetConnectionToast)
            }
        }
    }

    private fun loginUser() {
        progressDialog.show()
        binding.tvError.isGone()
        auth.signInWithEmailAndPassword(
            binding.inputEditEmail.text.toString() ,
            binding.inputEditPassword.text.toString()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (checkIfEmailVerified()) {
                        prefs.setFirstTimeLaunch(USER)
                        prefs.saveCurrentUserId(currentUser?.uid)
                        activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                        progressDialog.dismiss()
                    } else if (!checkIfEmailVerified()) {
                        makeErrorTextVisible(R.string.verify_account , R.color.red)
                    }
                } else {
                    makeErrorTextVisible(R.string.login_failed , R.color.red)
                }
            }
    }

    private fun clickSignUp() {
        binding.buttonSignUp.setOnClickListener {
            progressDialog.show()
            if (checkField(binding.inputEditEmail , binding.inputEditPassword)){
                checkInternetConnection(this::createNewUser, requireContext(), this::showNoInternetConnectionToast)
            }
        }
    }

    private fun createNewUser() {
        val email = binding.inputEditEmail.text.toString()
        auth
            .createUserWithEmailAndPassword(
                email ,
                binding.inputEditPassword.text.toString()
            )
        sendVerificationEmail()
        addNewUserToFirestore()
    }

    private fun sendVerificationEmail() {
        currentUser?.sendEmailVerification()
            ?.addOnSuccessListener {
                progressDialog.dismiss()
                showAlertDialog(requireContext() , this,
                    resources.getString(R.string.verify_your_email),
                    resources.getString(R.string.verify_your_email_subtitle),
                    resources.getString(R.string.ok))
                makeErrorTextVisible(R.string.verify_your_email_subtitle, R.color.grey80)
                deleteUnverifiedUser()
            }
            ?.addOnFailureListener { e ->
                makeErrorTextVisible(R.string.verification_failed , R.color.red)
            }
    }


    private fun clickForgotPassword() {
        binding.buttonForgotPassword.setOnClickListener {
            progressDialog.show()
            if (checkField(binding.inputEditEmail , binding.inputEditPassword)){
                checkInternetConnection(this::resetPassword, requireContext(), this::showNoInternetConnectionToast)
            }
        }
    }

    private fun resetPassword() {
        auth.sendPasswordResetEmail(binding.inputEditEmail.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    makeErrorTextVisible(R.string.password_reset_email , R.color.grey80)
                } else {
                    makeErrorTextVisible(R.string.not_authorized_email , R.color.red)
                }
            }
    }

    private fun deleteUnverifiedUser() {
        Handler().postDelayed({
            if (!checkIfEmailVerified()) {
                currentUser?.delete()
                usersCollection.document(currentUser!!.uid).delete()
            }
        }, 300000)
    }

    private fun checkIfEmailVerified(): Boolean {
        return currentUser?.isEmailVerified ?: false
    }

    private fun checkField(et1: EditText , et2: EditText): Boolean {
        if (et1.text.toString().isNullOrBlank() && et2.text.toString().isNullOrBlank()) {
            et1.background = resources.getDrawable(R.drawable.bg_field_red)
            et2.background = resources.getDrawable(R.drawable.bg_field_red)
            makeErrorTextVisible(R.string.fill_in_the_field , R.color.red)
            return false
        } else if (et1.text.toString().isNullOrBlank()) {
            et1.background = resources.getDrawable(R.drawable.bg_field_red)
            makeErrorTextVisible(R.string.fill_in_the_field , R.color.red)
            return false
        } else if (! isValidEmail(et1.text.toString())) {
            et1.background = resources.getDrawable(R.drawable.bg_field_red)
            makeErrorTextVisible(R.string.not_valid_email , R.color.red)
            return false
        } else if (et2.text.toString().isNullOrBlank()) {
            et2.background = resources.getDrawable(R.drawable.bg_field_red)
            makeErrorTextVisible(R.string.fill_in_the_field , R.color.red)
            return false
        } else if (et2.text.toString().length < 6) {
            et2.background = resources.getDrawable(R.drawable.bg_field_red)
            makeErrorTextVisible(R.string.password_characters , R.color.red)
            return false
        }
        binding.tvError.isGone()
        return true
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return ! TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun makeErrorTextVisible(stringUrl: Int , colorUrl: Int) {
        val textView = binding.tvError
        textView.text = resources.getString(stringUrl)
        textView.setTextColor(resources.getColor(colorUrl))
        textView.isVisible()
        progressDialog.dismiss()
    }

    private fun addNewUserToFirestore(){
        val email = binding.inputEditEmail.text.toString()
        currentUser?.uid?.let {
            usersCollection.document(it).set(mapOf("email" to email))
            createDefaultItems(it)}
    }

    private fun createDefaultItems(uid: String) {
        viewModel.createDefaultCategory(uid, getString(R.string.transport))
        viewModel.createDefaultGoal(uid, getString(R.string.your_goal))
        viewModel.createInfoDoc(uid)
    }

    private fun setupTextWatcher(et1: EditText , et2: EditText) {
        et1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(
                s: CharSequence? ,
                start: Int ,
                count: Int ,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence? , start: Int , before: Int , count: Int) {
                et1.background = context?.resources?.getDrawable(R.drawable.bg_field_gray)
                et2.background = context?.resources?.getDrawable(R.drawable.bg_field_gray)
                binding.tvError.isGone()
            }
        })
    }

    private fun showNoInternetConnectionToast() { requireContext().showNoConnectionToast() }

    override fun subscribeToLiveData() {}
}
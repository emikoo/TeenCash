package com.teenteen.teencash.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.teenteen.teencash.data.local.PrefsSettings
import com.teenteen.teencash.presentation.utills.ProgressDialog

abstract class BaseFragment<VB_CHILD : ViewBinding> : Fragment() {

    private var _binding: VB_CHILD? = null
    lateinit var binding: VB_CHILD
    lateinit var progressDialog: Dialog
    lateinit var prefs: PrefsSettings
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var currentUser: FirebaseUser? = null
    lateinit var usersCollection: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ) = getInflatedView(inflater , container , false)

    private fun getInflatedView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ): View {
        val tempList = mutableListOf<VB_CHILD>()
        attachBinding(tempList , inflater , container , attachToRoot)
        this._binding = tempList[0]
        binding = _binding as VB_CHILD
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        init()
        setupViews()
        subscribeToLiveData()
    }

    abstract fun setupViews()
    abstract fun subscribeToLiveData()

    private fun init() {
        db = FirebaseFirestore.getInstance()
        prefs = PrefsSettings(requireActivity())
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser
        progressDialog = ProgressDialog.progressDialog(requireActivity())
        usersCollection = db.collection("users")
    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    abstract fun attachBinding(
        list: MutableList<VB_CHILD> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    )
}
package com.teenteen.teencash.presentation.ui.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.teenteen.teencash.databinding.FragmentHomeBinding
import com.teenteen.teencash.presentation.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun attachBinding(
        list: MutableList<FragmentHomeBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentHomeBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        val usersRef = db.collection("users")
        usersRef.document(prefs.getCurrentUserId()).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    val email = document.getString("email")
                    binding.tv.text = email
                }
            }
        }
    }
}
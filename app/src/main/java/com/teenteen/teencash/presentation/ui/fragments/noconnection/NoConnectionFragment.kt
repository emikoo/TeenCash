package com.teenteen.teencash.presentation.ui.fragments.noconnection

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.teenteen.teencash.R
import com.teenteen.teencash.databinding.FragmentNoConnectionBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.ui.activity.MainActivity

class NoConnectionFragment() : BaseFragment<FragmentNoConnectionBinding>() {
    override fun setupViews() {
        binding.btnRetry.setOnClickListener {
            val intent = Intent(requireContext() , MainActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.default_enter_anim , R.anim.default_exit_anim)
            activity?.finishAffinity()
        }
    }

    override fun attachBinding(
        list: MutableList<FragmentNoConnectionBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentNoConnectionBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun subscribeToLiveData() {}
}
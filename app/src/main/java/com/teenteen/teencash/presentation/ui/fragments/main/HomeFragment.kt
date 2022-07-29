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

    override fun setupViews() {}
}
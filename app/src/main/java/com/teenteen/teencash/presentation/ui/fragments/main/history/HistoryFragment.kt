package com.teenteen.teencash.presentation.ui.fragments.main.history

import android.view.LayoutInflater
import android.view.ViewGroup
import com.teenteen.teencash.databinding.FragmentSpendingBinding
import com.teenteen.teencash.presentation.base.BaseFragment

class HistoryFragment : BaseFragment<FragmentSpendingBinding>() {
    override fun setupViews() {

    }

    override fun subscribeToLiveData() {

    }

    override fun attachBinding(
        list: MutableList<FragmentSpendingBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentSpendingBinding.inflate(layoutInflater , container , attachToRoot))
    }
}
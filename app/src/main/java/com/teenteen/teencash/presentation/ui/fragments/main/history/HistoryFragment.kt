package com.teenteen.teencash.presentation.ui.fragments.main.history

import android.view.LayoutInflater
import android.view.ViewGroup
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.databinding.FragmentHistoryBinding
import com.teenteen.teencash.presentation.base.BaseFragment

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    var historyArray = mutableListOf<History>()
    lateinit var adapter: HistoryAdapter

    override fun setupViews() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter(historyArray)
        binding.recyclerView.adapter = adapter
    }

    override fun subscribeToLiveData() {
        
    }

    override fun attachBinding(
        list: MutableList<FragmentHistoryBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentHistoryBinding.inflate(layoutInflater , container , attachToRoot))
    }
}
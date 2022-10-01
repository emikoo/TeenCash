package com.teenteen.teencash.presentation.ui.fragments.main.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.databinding.FragmentHistoryBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.view_model.HistoryViewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    var historyArray = mutableListOf<History>()
    lateinit var adapter: HistoryAdapter
    lateinit var viewModel: HistoryViewModel

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        viewModel.getHistory(prefs.getCurrentUserId())
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter(historyArray)
        binding.recyclerView.adapter = adapter
    }

    override fun subscribeToLiveData() {
        viewModel.history.observe(viewLifecycleOwner, Observer {
            historyArray.clear()
            historyArray.addAll(it)
            adapter.notifyDataSetChanged()
        })
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
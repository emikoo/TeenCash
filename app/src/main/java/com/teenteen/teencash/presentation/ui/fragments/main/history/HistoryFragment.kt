package com.teenteen.teencash.presentation.ui.fragments.main.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.databinding.FragmentHistoryBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.extensions.getCurrentDate
import com.teenteen.teencash.presentation.extensions.getCurrentMonth
import com.teenteen.teencash.presentation.extensions.getCurrentWeek
import com.teenteen.teencash.presentation.utills.internetIsConnected
import com.teenteen.teencash.view_model.HistoryViewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    var historyArray = mutableListOf<History>()
    lateinit var adapter: HistoryAdapter
    lateinit var viewModel: HistoryViewModel

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        progressDialog.show()
        viewModel.getHistory(prefs.getCurrentUserId())
        setupRecyclerView()
        setupSpinner()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter(historyArray)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSpinner() {
        val adapter =
            ArrayAdapter.createFromResource(requireActivity(), R.array.spinner_date , R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>? , p1: View? , p2: Int , p3: Long) {
                when (binding.spinner.selectedItemPosition) {
                    0 -> viewModel.getHistory(prefs.getCurrentUserId())
                    1 -> viewModel.getHistoryToday(prefs.getCurrentUserId(), getCurrentDate())
                    2 -> viewModel.getHistoryByRange(prefs.getCurrentUserId(), getCurrentWeek())
                    3 -> viewModel.getHistoryByMonth(prefs.getCurrentUserId(), getCurrentMonth())
                    else -> viewModel.getHistory(prefs.getCurrentUserId())
                }
            }
        }
    }

    override fun subscribeToLiveData() {
        if (internetIsConnected(requireContext())) {
            viewModel.history.observe(viewLifecycleOwner, Observer {
                historyArray.clear()
                historyArray.addAll(it.sortedByDescending { it.time })
                adapter.notifyDataSetChanged()
                progressDialog.dismiss()
            })
        } else Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
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
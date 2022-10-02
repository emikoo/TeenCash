package com.teenteen.teencash.presentation.ui.fragments.main.debtors

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Debtor
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.databinding.FragmentDebtorsBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.extensions.*
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.ui.common_bottom_sheets.BottomSheetAdd
import com.teenteen.teencash.presentation.utills.AddBottomSheetKeys
import com.teenteen.teencash.presentation.utills.DebtorAdapterKeys
import com.teenteen.teencash.presentation.utills.checkInternetConnection
import com.teenteen.teencash.presentation.utills.internetIsConnected
import com.teenteen.teencash.view_model.MainViewModel

class DebtorsFragment : BaseFragment<FragmentDebtorsBinding>(), UpdateData, DebtorsAdapter.DebtorClickListener {

    private lateinit var adapter: DebtorsAdapter
    lateinit var viewModel: MainViewModel
    private var mfArray = mutableListOf<Debtor>()
    private var bsArray = mutableListOf<Debtor>()
    var key: DebtorAdapterKeys = DebtorAdapterKeys.MOTHERFUCKER
    var balance = 0

    override fun attachBinding(
        list: MutableList<FragmentDebtorsBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentDebtorsBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupTabLayout()
        createDebtor()
        setupRecyclerView(mfArray, DebtorAdapterKeys.MOTHERFUCKER)
        viewModel.getMotherfuckers(prefs.getCurrentUserId())
        viewModel.getBloodsuckers(prefs.getCurrentUserId())
        viewModel.getBalance(prefs.getCurrentUserId())
    }

    private fun setupTabLayout() {
        val tabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.borrowers)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.moneylenders)))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tabLayout.selectedTabPosition == 0) {
                    key = DebtorAdapterKeys.MOTHERFUCKER
                    viewModel.getMotherfuckers(prefs.getCurrentUserId())
                    setupRecyclerView(mfArray, key)
                } else if (tabLayout.selectedTabPosition == 1) {
                    key = DebtorAdapterKeys.BLOODSUCKER
                    viewModel.getBloodsuckers(prefs.getCurrentUserId())
                    setupRecyclerView(bsArray, key)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupRecyclerView(array: MutableList<Debtor>, key: DebtorAdapterKeys) {
        adapter = DebtorsAdapter(array, this, key)
        binding.recyclerView.adapter = adapter
    }

    private fun createDebtor() {
        binding.btnAdd.setOnClickListener {
            when (key) {
                DebtorAdapterKeys.MOTHERFUCKER -> BottomSheetAdd(this, AddBottomSheetKeys.CREATE_MOTHERFUCKER)
                    .show(activity?.supportFragmentManager)
                DebtorAdapterKeys.BLOODSUCKER -> BottomSheetAdd(this, AddBottomSheetKeys.CREATE_BLOODSUCKER)
                    .show(activity?.supportFragmentManager)
            }
        }
    }

    override fun deleteDebtor(item: Debtor, key: DebtorAdapterKeys) {
        var newBalance = 0
        if (internetIsConnected(requireContext())) {
            when (key) {
                DebtorAdapterKeys.MOTHERFUCKER -> {
                    viewModel.deleteMotherfucker(prefs.getCurrentUserId(), item.docName)
                    newBalance = balance + item.amount
                    if (item.amount != 0) viewModel.putToHistory(prefs.getCurrentUserId(),
                        History(item.name, item.amount, false, getCurrentDate() ,
                            getCurrentDateTime(), getCurrentMonth(),666, item.currency.toString()))
                    viewModel.updateBalance(prefs.getCurrentUserId(), newBalance)
                    updateMFList()
                }
                DebtorAdapterKeys.BLOODSUCKER -> {
                    viewModel.deleteBloodsucker(prefs.getCurrentUserId(), item.docName)
                    newBalance = balance - item.amount
                    if (item.amount != 0) viewModel.putToHistory(prefs.getCurrentUserId(),
                        History(item.name, item.amount, true, getCurrentDate(),
                            getCurrentDateTime(), getCurrentMonth(),666, item.currency.toString()))
                    viewModel.updateBalance(prefs.getCurrentUserId(), newBalance)
                    updateBSList()
                }
            }
        } else Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
    }

    override fun editDebtor(item: Debtor, key: DebtorAdapterKeys) {
        if (internetIsConnected(requireContext())) {
            when (key) {
                DebtorAdapterKeys.MOTHERFUCKER -> {
                    BottomSheetAdd(this, AddBottomSheetKeys.UPDATE_MOTHERFUCKER, itemDebtor = item)
                        .show(activity?.supportFragmentManager)
                }
                DebtorAdapterKeys.BLOODSUCKER -> {
                    BottomSheetAdd(this, AddBottomSheetKeys.UPDATE_BLOODSUCKER, itemDebtor = item)
                        .show(activity?.supportFragmentManager)
                }
            }
        } else Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
    }
    override fun updateMFList() {
        viewModel.getMotherfuckers(prefs.getCurrentUserId())
    }

    override fun updateBSList() {
        viewModel.getBloodsuckers(prefs.getCurrentUserId())
    }

    override fun subscribeToLiveData() {
        if (internetIsConnected(requireContext())) {
            viewModel.mf.observe(viewLifecycleOwner , Observer {
                updateArray(mfArray , it)
                progressDialog.dismiss()
            })
            viewModel.bs.observe(viewLifecycleOwner , Observer {
                updateArray(bsArray , it)
                progressDialog.dismiss()
            })
            viewModel.balance.observe(viewLifecycleOwner, Observer { balance = it })
        } else Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
    }
    private fun updateArray(array: MutableList<Debtor> , newList: List<Debtor>) {
        array.clear()
        array.addAll(newList)
        adapter.notifyDataSetChanged()
    }

    override fun achieved() {}
    override fun updateCategory() {}
    override fun updatePiggyBank() {}
    override fun updateStatistics() {}
}

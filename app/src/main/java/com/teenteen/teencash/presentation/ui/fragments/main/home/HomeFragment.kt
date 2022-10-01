package com.teenteen.teencash.presentation.ui.fragments.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.FragmentHomeBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.extensions.dateToString
import com.teenteen.teencash.presentation.extensions.getCurrentDateTime
import com.teenteen.teencash.presentation.extensions.show
import com.teenteen.teencash.presentation.extensions.showNoConnectionToast
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.ui.common_bottom_sheets.BottomSheetAdd
import com.teenteen.teencash.presentation.ui.common_bottom_sheets.BottomSheetList
import com.teenteen.teencash.presentation.ui.fragments.main.home.adapters.CategoryAdapter
import com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets.AddCategoryBS
import com.teenteen.teencash.presentation.utills.AddBottomSheetKeys
import com.teenteen.teencash.presentation.utills.CategoryAdapterKeys
import com.teenteen.teencash.presentation.utills.ListBottomSheetKeys
import com.teenteen.teencash.presentation.utills.checkInternetConnection
import com.teenteen.teencash.view_model.MainViewModel
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class HomeFragment : BaseFragment<FragmentHomeBinding>() , CategoryAdapter.CategoryClickListener ,
    UpdateData {

    private var categoryArray = mutableListOf<Category>()
    private var piggyArray = mutableListOf<Category>()
    private lateinit var categoryAdapter: CategoryAdapter
    lateinit var viewModel: MainViewModel
    var spentToday = 0

    override fun attachBinding(
        list: MutableList<FragmentHomeBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentHomeBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupTabLayout()
        checkInternetConnection(this::getData , requireContext(), this::showNoInternetConnectionToast)
        setupRecyclerView(categoryArray , CategoryAdapterKeys.CATEGORY)
        addBalance()
        binding.ibEditLimit.setOnClickListener {
            BottomSheetAdd(this, AddBottomSheetKeys.SET_LIMIT)
                .show(activity?.supportFragmentManager)
        }
        binding.totalAmount.setOnLongClickListener {
            BottomSheetAdd(this, AddBottomSheetKeys.UPDATE_BALANCE)
                .show(activity?.supportFragmentManager)
            true
        }
    }

    private fun setupTabLayout() {
        val tabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.spendings)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.piggy_banks)))
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tabLayout.selectedTabPosition == 0) {
                    setupRecyclerView(categoryArray , CategoryAdapterKeys.CATEGORY)
                } else if (tabLayout.selectedTabPosition == 1) {
                    setupRecyclerView(piggyArray , CategoryAdapterKeys.PIGGY_BANK)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupRecyclerView(array: MutableList<Category> , key: CategoryAdapterKeys) {
        categoryAdapter = CategoryAdapter(array , this , key)
        binding.recyclerView.adapter = categoryAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext() , 3)
    }

    private fun getData() {
        progressDialog.show()
        checkDate()
        viewModel.getBalance(prefs.getCurrentUserId())
        viewModel.getSavedAmount(prefs.getCurrentUserId())
        viewModel.getLimit(prefs.getCurrentUserId())
        viewModel.getSpentAmount(prefs.getCurrentUserId())
        viewModel.getCategories(prefs.getCurrentUserId())
        viewModel.getPiggyBanks(prefs.getCurrentUserId())
    }

    private fun addBalance() {
        binding.btnTotalEdit.setOnClickListener {
            BottomSheetAdd(
                this , AddBottomSheetKeys.CURRENT_BALANCE
            ).show(activity !!.supportFragmentManager)
        }
    }

    private fun checkDate() {
        val date = getCurrentDateTime()
        val dateInString = date.dateToString()
        if (prefs.getCurrentDay() != dateInString) {
            viewModel.updateSpentAmount(prefs.getCurrentUserId(), 0)
            viewModel.getSpentAmount(prefs.getCurrentUserId())
        }
    }

    override fun onAddCategoryClickListener(item: Category) {
        AddCategoryBS(this).show(activity?.supportFragmentManager)
    }

    override fun onAddPiggyClickListener(item: Category) {
        BottomSheetAdd(
            this ,
            AddBottomSheetKeys.ADD_PIGGY_BANK
        ).show(activity?.supportFragmentManager)
    }

    override fun onCategoryDotsClickListener(item: Category) {
        BottomSheetList(ListBottomSheetKeys.CATEGORY_SETTINGS, this, itemCategory = item)
            .show(activity?.supportFragmentManager)
    }

    override fun onPiggyDotsClickListener(item: Category) {
        BottomSheetList(ListBottomSheetKeys.PIGGY_BANK_SETTINGS, this, itemCategory = item)
            .show(activity?.supportFragmentManager)
    }

    override fun onCategoryClickListener(item: Category) {
        BottomSheetAdd(this , AddBottomSheetKeys.SPENT_CATEGORY , item)
            .show(activity?.supportFragmentManager)
    }

    override fun onPiggyClickListener(item: Category) {
        BottomSheetAdd(this , AddBottomSheetKeys.SAVED_PIGGY , item)
            .show(activity?.supportFragmentManager)
    }

    private fun updateArray(array: MutableList<Category>, newList: List<Category>) {
        array.clear()
        array.addAll(newList)
        array.add(Category(0 , 0 , "", "",0))
        categoryAdapter.notifyDataSetChanged()
    }

    override fun updateCategory() {
        progressDialog.show()
        viewModel.getCategories(prefs.getCurrentUserId())
        viewModel.getBalance(prefs.getCurrentUserId())
        viewModel.getSpentAmount(prefs.getCurrentUserId())
    }
    override fun updatePiggyBank() {
        progressDialog.show()
        viewModel.getPiggyBanks(prefs.getCurrentUserId())
        viewModel.getBalance(prefs.getCurrentUserId())
        viewModel.getSavedAmount(prefs.getCurrentUserId())
    }
    override fun updateStatistics() {
        progressDialog.show()
        viewModel.getBalance(prefs.getCurrentUserId())
        viewModel.getSavedAmount(prefs.getCurrentUserId())
        viewModel.getLimit(prefs.getCurrentUserId())
        viewModel.getSpentAmount(prefs.getCurrentUserId())
    }

    override fun subscribeToLiveData() {
        viewModel.category.observe(viewLifecycleOwner) {
            val date = getCurrentDateTime()
            val dateInString = date.dateToString()
            if (prefs.getCurrentDay() != dateInString) {
                for (item in it) {
                    viewModel.clearAmountCategory(prefs.getCurrentUserId() , item.docName)
                }
            }
            updateArray(categoryArray , it)
            prefs.saveCurrentDay(dateInString)
            progressDialog.dismiss()
        }
        viewModel.piggy.observe(viewLifecycleOwner) {
            updateArray(piggyArray , it)
            progressDialog.dismiss()
        }
        viewModel.balance.observe(viewLifecycleOwner) {
            if (it == null || it == 0) binding.totalAmount.text = "0"
            else binding.totalAmount.text = it.toString()
            progressDialog.dismiss()
        }
        viewModel.saved.observe(viewLifecycleOwner) {
            if (it == null || it == 0) binding.saved.text = "0"
            else binding.saved.text = it.toString()
            progressDialog.dismiss()
        }
        viewModel.limit.observe(viewLifecycleOwner) {
            if (it == null || it == 0) binding.limit.text = "0"
            else binding.limit.text = it.toString()
            progressDialog.dismiss()
        }
        viewModel.spentAmount.observe(viewLifecycleOwner) {
            if (it == null || it == 0) binding.progressTitle.text = "0/${binding.limit.text}\nKGS"
            else if (it.toString().length >= 3 && binding.limit.text.length >= 5) {
                binding.progressTitle.text = "$it/\n${binding.limit.text}\nKGS"
            } else if (it.toString().length >= 5 && binding.limit.text.length >= 3) {
                binding.progressTitle.text = "$it/\n${binding.limit.text}\nKGS"
            } else binding.progressTitle.text = "$it/${binding.limit.text}\nKGS"
            progressDialog.dismiss()
            if (binding.limit.text.toString() == "") binding.progressCircular.max = 0
            else binding.progressCircular.max = binding.limit.text.toString().toInt()
            binding.progressCircular.progress = it
            spentToday = it
        }
    }

    private fun showNoInternetConnectionToast() { requireContext().showNoConnectionToast() }

    override fun updateBSList() {}
    override fun achieved() {
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        binding.konfettiView.start(party)
    }

    override fun updateMFList() {}
}
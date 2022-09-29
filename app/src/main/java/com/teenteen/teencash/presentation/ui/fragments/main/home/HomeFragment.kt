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
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.ui.common_bottom_sheets.BottomSheetAdd
import com.teenteen.teencash.presentation.ui.common_bottom_sheets.BottomSheetList
import com.teenteen.teencash.presentation.ui.fragments.main.home.adapters.CategoryAdapter
import com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets.AddCategoryBS
import com.teenteen.teencash.presentation.utills.AddBottomSheetKeys
import com.teenteen.teencash.presentation.utills.CategoryAdapterKeys
import com.teenteen.teencash.presentation.utills.ListBottomSheetKeys
import com.teenteen.teencash.presentation.utills.checkInternetConnection
import com.teenteen.teencash.view_model.UserProfileViewModel
import com.teenteen.teencash.view_model.MainViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() , CategoryAdapter.CategoryClickListener ,
    UpdateData {

    private var categoryArray = mutableListOf<Category>()
    private var piggyArray = mutableListOf<Category>()
    private lateinit var categoryAdapter: CategoryAdapter
    lateinit var viewModel: MainViewModel

    override fun attachBinding(
        list: MutableList<FragmentHomeBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentHomeBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupTabLayout()
        checkInternetConnection(this::getData , requireContext())
        setupRecyclerView(categoryArray , CategoryAdapterKeys.CATEGORY)
    }

    private fun getData(){
        getCategories()
        getPiggies()
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
        viewModel.getCategories(prefs.getCurrentUserId())
    }

    private fun getPiggies() {
        progressDialog.show()
        viewModel.getPiggyBanks(prefs.getCurrentUserId())
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
            AddBottomSheetKeys.PIGGY_BANK
        ).show(activity?.supportFragmentManager)
    }

    override fun onCategoryDotsClickListener(item: Category) {
        BottomSheetList(ListBottomSheetKeys.CATEGORY_SETTINGS, this, item.name)
            .show(activity?.supportFragmentManager)
    }

    override fun onPiggyDotsClickListener(item: Category) {
        BottomSheetList(ListBottomSheetKeys.PIGGY_BANK_SETTINGS, this, item.name)
            .show(activity?.supportFragmentManager)
    }

    override fun onCategoryClickListener(item: Category) {

    }

    override fun onPiggyClickListener(item: Category) {
    }

    private fun updateArray(array: MutableList<Category>, newList: List<Category>) {
        array.clear()
        array.addAll(newList)
        array.add(Category(0 , 0 , "", "",0))
        categoryAdapter.notifyDataSetChanged()
    }
    override fun subscribeToLiveData() {
        viewModel.category.observe(viewLifecycleOwner) {
            val date = getCurrentDateTime()
            val dateInString = date.dateToString()
            if (prefs.getCurrentDay() != dateInString) {
                for (item in it) {
                    viewModel.clearAmountCategory(prefs.getCurrentUserId() , item.name)
                }
            }
            updateArray(categoryArray , it)
            progressDialog.dismiss()
        }
    }
}
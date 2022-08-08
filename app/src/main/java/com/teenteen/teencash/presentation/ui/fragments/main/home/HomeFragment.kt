package com.teenteen.teencash.presentation.ui.fragments.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.FragmentHomeBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.extensions.show
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.ui.bottom_sheets.BottomSheetSettings
import com.teenteen.teencash.presentation.ui.fragments.main.home.adapters.CategoryAdapter
import com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets.AddCategoryBS
import com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets.AddPiggyBS
import com.teenteen.teencash.presentation.utills.checkInternetConnection
import com.teenteen.teencash.view_model.UserProfileViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() , CategoryAdapter.CategoryClickListener ,
    UpdateData {

    private var categoryArray = mutableListOf<Category>()
    private var piggyArray = mutableListOf<Category>()
    private lateinit var categoryAdapter: CategoryAdapter
    lateinit var viewModel: UserProfileViewModel
    lateinit var key: String

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
        key = CATEGORY_KEY
        setupTabLayout()
        checkInternetConnection(this::getData, requireContext())
        setupRecyclerView(categoryArray, CATEGORY_KEY)
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
                if (tabLayout.selectedTabPosition == 0){
                    setupRecyclerView(categoryArray, CATEGORY_KEY)
                }
                else if (tabLayout.selectedTabPosition == 1) {
                    setupRecyclerView(piggyArray, PIGGY_BANK_KEY)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupRecyclerView(array: MutableList<Category>, key: String) {
        categoryAdapter = CategoryAdapter(array , this, key)
        binding.recyclerView.adapter = categoryAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext() , 4)
    }

    override fun updateCategory() {
        getCategories()
    }

    override fun updatePiggyBank() {
        getPiggies()
    }

    private fun getCategories() {
        progressDialog.show()
        viewModel.getCategories(prefs.getCurrentUserId())
    }

    private fun getPiggies() {
        progressDialog.show()
        viewModel.getPiggyBanks(prefs.getCurrentUserId())
    }

    override fun onAddCategoryClickListener(item: Category) {
        AddCategoryBS(this).show(activity?.supportFragmentManager)
    }

    override fun onAddPiggyClickListener(item: Category) {
        AddPiggyBS(this, PIGGY_BANK_KEY).show(activity?.supportFragmentManager)
    }

    override fun onCategoryDotsClickListener(item: Category) {
        BottomSheetSettings(BottomSheetSettings.CATEGORY_SETTINGS, this, item.name).show(activity?.supportFragmentManager)
    }

    override fun onPiggyDotsClickListener(item: Category) {
        BottomSheetSettings(BottomSheetSettings.PIGGY_SETTINGS, this, item.name).show(activity?.supportFragmentManager)
    }

    override fun subscribeToLiveData() {
        viewModel.category.observe(viewLifecycleOwner , Observer {
            updateArray(categoryArray, it)
        })
        viewModel.piggy.observe(viewLifecycleOwner , Observer {
            updateArray(piggyArray, it)
        })
    }

    private fun updateArray(array: MutableList<Category>, newList: List<Category>) {
        array.clear()
        array.addAll(newList)
        array.add(Category(0 , 0 , ""))
        categoryAdapter.notifyDataSetChanged()
        progressDialog.dismiss()
    }

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
        const val PIGGY_BANK_KEY = "PIGGY_BANK_KEY"
        const val SPENT_CATEGORY_KEY = "SPENT_CATEGORY_KEY"
        const val SAVED_PIGGY_KEY = "SAVED_PIGGY_KEY"
        const val SET_LIMIT_KEY = "SET_LIMIT_KEY"
    }
}
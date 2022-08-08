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
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.ui.fragments.main.home.adapters.CategoryAdapter
import com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets.AddCategoryBS
import com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets.AddPiggyBS
import com.teenteen.teencash.view_model.UserProfileViewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>() , CategoryAdapter.AddClickListener ,
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
        getCategories()
        getPiggies()
        setupRecyclerView(categoryArray, CATEGORY_KEY)
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

    private fun getCategories() {
        progressDialog.show()
        viewModel.getCategories(prefs.getCurrentUserId())
    }

    private fun getPiggies() {
        progressDialog.show()
        viewModel.getPiggyBanks(prefs.getCurrentUserId())
    }

    override fun updateCategory(newCategory: Category) {
        getCategories()
    }

    override fun updatePiggyBank(newGoal: Category) {
        getPiggies()
    }

    override fun onAddCategoryClickListener(item: Category) {
        val bottomSheetDialogFragment: BottomSheetDialogFragment =
            AddCategoryBS(this)
        activity?.supportFragmentManager?.let {
            bottomSheetDialogFragment.show(
                it ,
                bottomSheetDialogFragment.tag
            )
        }
    }

    override fun onAddPiggyClickListener(item: Category) {
        val bottomSheetDialogFragment: BottomSheetDialogFragment =
            AddPiggyBS(this, PIGGY_BANK_KEY)
        activity?.supportFragmentManager?.let {
            bottomSheetDialogFragment.show(
                it ,
                bottomSheetDialogFragment.tag
            )
        }
    }

    override fun subscribeToLiveData() {
        viewModel.category.observe(viewLifecycleOwner , Observer {
            categoryArray.clear()
            categoryArray.addAll(it)
            categoryArray.add(Category(0 , 0 , ""))
            categoryAdapter.notifyDataSetChanged()
            progressDialog.dismiss()
        })
        viewModel.piggy.observe(viewLifecycleOwner , Observer {
            piggyArray.clear()
            piggyArray.addAll(it)
            piggyArray.add(Category(0 , 0 , ""))
            categoryAdapter.notifyDataSetChanged()
            progressDialog.dismiss()
        })
    }

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
        const val PIGGY_BANK_KEY = "PIGGY_BANK_KEY"
        const val SPENT_CATEGORY_KEY = "SPENT_CATEGORY_KEY"
        const val SAVED_PIGGY_KEY = "SAVED_PIGGY_KEY"
        const val SET_LIMIT_KEY = "SET_LIMIT_KEY"
    }
}
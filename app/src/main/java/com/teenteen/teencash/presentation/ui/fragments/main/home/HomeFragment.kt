package com.teenteen.teencash.presentation.ui.fragments.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.FragmentHomeBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.ui.fragments.main.home.adapters.CategoryAdapter
import com.teenteen.teencash.view_model.UserProfileViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() , CategoryAdapter.AddClickListener ,
    UpdateData {

    private var categoryArray = mutableListOf<Category>()
    private lateinit var categoryAdapter: CategoryAdapter
    lateinit var viewModel: UserProfileViewModel

    override fun attachBinding(
        list: MutableList<FragmentHomeBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentHomeBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        setupRecyclerView()
        setupTabLayout()
        getCategories()
    }

    private fun getCategories() {
        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        viewModel.getCategories(prefs.getCurrentUserId())
        progressDialog.show()
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(categoryArray , this)
        binding.recyclerView.adapter = categoryAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext() , 4)
    }

    private fun setupTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.spendings)))
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.piggy_banks))
        )
    }

    override fun subscribeToLiveData() {
        viewModel.category.observe(viewLifecycleOwner , Observer {
            categoryArray.clear()
            categoryArray.addAll(it)
            categoryArray.add(Category(0 , 0 , ""))
            categoryAdapter.notifyDataSetChanged()
            progressDialog.dismiss()
        })
    }

    override fun onAddClickListener(item: Category) {
        val bottomSheetDialogFragment: BottomSheetDialogFragment =
            AddCategoryBS(this)
        bottomSheetDialogFragment.isCancelable = false
        activity?.supportFragmentManager?.let {
            bottomSheetDialogFragment.show(
                it ,
                bottomSheetDialogFragment.tag
            )
        }
    }

    override fun updateCategory(newCategory: Category) {
        categoryArray.add(categoryArray.size -1, newCategory)
        categoryAdapter.notifyDataSetChanged()
    }
}
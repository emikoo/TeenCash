package com.teenteen.teencash.presentation.ui.fragments.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.FragmentHomeBinding
import com.teenteen.teencash.presentation.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(), CategoryAdapter.AddClickListener {

    private var categoryArray = mutableListOf<Category>(Category(0,"", 0, 0))
    private lateinit var categoryAdapter: CategoryAdapter

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

//        val usersRef = db.collection("users")
//        usersRef.document(prefs.getCurrentUserId()).get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val document = task.result
//                if (document.exists()) {
//                    val email = document.getString("email")
////                    binding.tv.text = email
//                }
//            }
//        }
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(categoryArray, this)
        binding.recyclerView.adapter = categoryAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
    }

    private fun setupTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.spendings)))
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.piggy_banks))
        )
    }

    override fun onAddClickListener(item: Category) {

    }
}
package com.teenteen.teencash.presentation.ui.fragments.main.settings.achievement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.FragmentAchievementsBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.extensions.show
import com.teenteen.teencash.presentation.interfaces.UpdateAchievement
import com.teenteen.teencash.presentation.ui.fragments.main.settings.SettingsFragment
import com.teenteen.teencash.presentation.ui.fragments.main.settings.bottom_sheets.AchievementBottomSheet
import com.teenteen.teencash.view_model.MainViewModel

class AchievementsFragment() : BaseFragment<FragmentAchievementsBinding>(),
    AchievementAdapter.AchievementClickListener, UpdateAchievement {

    lateinit var adapter: AchievementAdapter
    lateinit var viewModel: MainViewModel
    private var array = listOf<Category>()

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        progressDialog.show()
        viewModel.getAchievements(prefs.getCurrentUserId())
    }

    private fun setupRecyclerView() {
        adapter = AchievementAdapter(array,this)
        binding.recyclerView.adapter = adapter
        if (array.isEmpty()) binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        else binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.ibBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onClickListener(item: Category) {
        AchievementBottomSheet(item, this).show(activity?.supportFragmentManager)
    }

    override fun subscribeToLiveData() {
        viewModel.achievement.observe(viewLifecycleOwner, Observer {
            array = it
            setupRecyclerView()
            progressDialog.dismiss()
        })
    }

    override fun updateAchievement() { viewModel.getAchievements(prefs.getCurrentUserId()) }

    override fun attachBinding(
        list: MutableList<FragmentAchievementsBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentAchievementsBinding.inflate(layoutInflater, container, attachToRoot))
    }
}
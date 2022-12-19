package com.teenteen.teencash.presentation.ui.fragments.main.settings.achievement

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.FragmentAchievementsBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.utills.internetIsConnected
import com.teenteen.teencash.presentation.utills.showAlertDialog
import com.teenteen.teencash.view_model.MainViewModel

class AchievementsFragment : BaseFragment<FragmentAchievementsBinding>() ,
    AchievementAdapter.AchievementClickListener {

    lateinit var adapter: AchievementAdapter
    lateinit var viewModel: MainViewModel
    private var array = listOf<Category>()
    var docName = ""

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        progressDialog.show()
        viewModel.getAchievements(prefs.getCurrentUserId())
        val directions = AchievementsFragmentDirections.actionAchievementsFragmentToSettingsFragment()
        binding.ibBack.setOnClickListener { findNavController().navigate(directions) }
    }

    private fun setupRecyclerView() {
        adapter = AchievementAdapter(array , this)
        binding.recyclerView.adapter = adapter
        if (array.isEmpty()) binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        else binding.recyclerView.layoutManager = GridLayoutManager(requireContext() , 3)
    }

    override fun onClickListener(item: Category) {
//        AchievementBottomSheet(item).show(activity?.supportFragmentManager)
    }

    override fun deleteAchievement(item: Category) {
        docName = item.docName
        showAlertDialog(
            requireContext() ,
            this ,
            titleText = getString(R.string.delete_achievement) ,
            subtitleText = getString(R.string.are_u_sure_delete) ,
            buttonText = getString(R.string.yes) ,
            action = this::delete
        )
    }
    //КАКБУДТО ТУТ МОЖНО ИЗБАВИТЬСЯ ОТ ЭТОГО ВСЕГО
    private fun delete() {
        if (internetIsConnected(requireContext())) {
            viewModel.deleteAchievement(prefs.getCurrentUserId() , docName)
            viewModel.getAchievements(prefs.getCurrentUserId())
        } else Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
    }

    override fun subscribeToLiveData() {
        if (internetIsConnected(requireContext())) {
            viewModel.achievement.observe(viewLifecycleOwner , Observer {
                array = it
                setupRecyclerView()
                progressDialog.dismiss()
            })
        } else Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val directions = AchievementsFragmentDirections.actionAchievementsFragmentToSettingsFragment()
                    findNavController().navigate(directions)
                }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun attachBinding(
        list: MutableList<FragmentAchievementsBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentAchievementsBinding.inflate(layoutInflater, container, attachToRoot))
    }
}
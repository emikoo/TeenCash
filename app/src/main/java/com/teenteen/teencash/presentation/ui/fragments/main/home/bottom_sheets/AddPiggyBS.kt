package com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.BsAddPiggyBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.extensions.isInvisible
import com.teenteen.teencash.presentation.extensions.isVisible
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.ui.fragments.main.home.HomeFragment

class AddPiggyBS(private val updater: UpdateData , val key: String) :
    BaseBottomSheetDialogFragment<BsAddPiggyBinding>() {

    var categoryName = ""
    var limit = 0

    override fun setupViews() {
        setupViewsByKey()
        setupListener()
    }

    private fun setupViewsByKey() {
        if (key == HomeFragment.PIGGY_BANK_KEY) {
            binding.title.isInvisible()
            binding.limit.isInvisible()
            binding.etName.isVisible()
        } else {
            binding.title.isVisible()
            binding.limit.isVisible()
            binding.etName.isInvisible()
        }
        when (key) {
            HomeFragment.PIGGY_BANK_KEY -> setupTextByKey(resources.getString(R.string.goal), false)
            HomeFragment.SET_LIMIT_KEY -> setupTextByKey(resources.getString(R.string.limit), false)
            HomeFragment.SPENT_CATEGORY_KEY -> setupTextByKey(resources.getString(R.string.spent), true)
            HomeFragment.SAVED_PIGGY_KEY -> setupTextByKey(resources.getString(R.string.saved), true)
        }
    }

    private fun setupTextByKey(etAmountHint: String, btnAddText: Boolean) {
        binding.etAmount.hint = etAmountHint
        if (btnAddText) binding.btnAdd.text = resources.getString(R.string.add)
        else binding.btnAdd.text = resources.getString(R.string.save)
    }

    private fun setupListener() {
        binding.btnAdd.setOnClickListener{
            when(key){
                HomeFragment.PIGGY_BANK_KEY -> {
                    checkFields()
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    private fun checkFields() {
        if (binding.etName.text.isNotBlank() && binding.etName.text.isNotEmpty()
            && binding.etAmount.text.isNotBlank() && binding.etAmount.text.isNotEmpty()
        ) {
            checkIfDocExists()
        } else Toast.makeText(requireContext() , getString(R.string.check_all_data) , Toast.LENGTH_LONG).show()
    }

    private fun checkIfDocExists() {
        categoryName = binding.etName.text.toString()
        limit = binding.etAmount.text.toString().toInt()
        val categoriesOfUser = usersCollection.document(prefs.getCurrentUserId())
            .collection("piggy_banks").document(categoryName)
        categoriesOfUser.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                    if (document.exists()) {
                        Toast.makeText(requireContext(), getString(R.string.item_exists), Toast.LENGTH_LONG).show()
                    } else {
                        addPiggy()
                    }
                }
            }
        }
    }

    private fun addPiggy() {
        val newGoal = Category(
            name = categoryName ,
            secondAmount = limit ,
            iconId = 777
        )
        dialog?.dismiss()
        updater.updatePiggyBank()
        usersCollection.document(prefs.getCurrentUserId())
            .collection("piggy_banks").document(categoryName).set(newGoal)
    }

    override fun attachBinding(
        list: MutableList<BsAddPiggyBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsAddPiggyBinding.inflate(layoutInflater , container , attachToRoot))
    }
}
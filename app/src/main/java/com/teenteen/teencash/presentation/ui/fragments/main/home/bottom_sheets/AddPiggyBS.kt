package com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
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
                    val newGoal = Category(
                        name = binding.etName.text.toString() ,
                        secondAmount = binding.etAmount.text.toString().toInt() ,
                        iconId = 777
                    )
                    dialog?.dismiss()
                    updater.updatePiggyBank(newGoal)
                    val piggiesOfUser = usersCollection.document(prefs.getCurrentUserId())
                        .collection("piggy_banks")
                    piggiesOfUser.add(newGoal)
                    dialog!!.dismiss()
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            dialog!!.dismiss()
        }
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
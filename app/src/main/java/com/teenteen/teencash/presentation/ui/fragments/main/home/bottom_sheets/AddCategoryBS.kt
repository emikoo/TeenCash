package com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.teencash.ui.bottom_sheet.icon.IconListBS
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.CategoryName
import com.teenteen.teencash.databinding.BsAddCategoryBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.extensions.isGone
import com.teenteen.teencash.presentation.extensions.isVisible
import com.teenteen.teencash.presentation.extensions.show
import com.teenteen.teencash.presentation.interfaces.PickerItem
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.utills.checkInternetConnection

class AddCategoryBS(private val updater: UpdateData , private val isSpending: Boolean) :
    BaseBottomSheetDialogFragment<BsAddCategoryBinding>() ,
    PickerItem {

    override fun attachBinding(
        list: MutableList<BsAddCategoryBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsAddCategoryBinding.inflate(layoutInflater , container , attachToRoot))
    }

    var iconId = 0

    override fun setupViews() {
        if (isSpending) {
            binding.etLimit.isVisible()
            binding.maxLimit.isVisible()
            binding.etCategoryName.setHint(R.string.name)
        }
        else {
            binding.etLimit.isGone()
            binding.maxLimit.isGone()
            binding.etCategoryName.setHint(R.string.set_source)
        }
        setupTextLimitations()
        checkInternetConnection(this::setupListeners, requireContext())
        binding.ibClose.setOnClickListener {
            dialog?.dismiss()
        }
        binding.tvCurrency.text = prefs.getSettingsCurrency()
    }

    private fun setupListeners() {
        binding.ibEdit.setOnClickListener {
            IconListBS(this).show(activity?.supportFragmentManager)
        }
        binding.btnAdd.setOnClickListener {
            checkFields()
        }
    }

    private fun checkFields() {
        if (binding.etCategoryName.text.isNotBlank() && binding.etLimit.text.isNotBlank() && isSpending) {
            checkIfDocExists("categories")
        } else if (binding.etCategoryName.text.isNotBlank() && !isSpending) checkIfDocExists("earnings")
        else Toast.makeText(requireContext() , getString(R.string.check_all_data) , Toast.LENGTH_LONG).show()
    }

    private fun checkIfDocExists(colName: String) {
        val categoriesOfUser = usersCollection.document(prefs.getCurrentUserId())
            .collection(colName).document(binding.etCategoryName.text.toString()+binding
                .etLimit.text.toString()+iconId.toString())
        categoriesOfUser.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                    if (document.exists()) {
                        Toast.makeText(requireContext(), getString(R.string.item_exists), Toast.LENGTH_LONG).show()
                    } else {
                        addCategory(colName)
                    }
                }
            }
        }
    }

    private fun addCategory(colName: String) {
        val limit = if (isSpending) binding.etLimit.text.toString().toInt()
                    else 0
        val docName = "${binding.etCategoryName.text}${binding.etLimit.text}$iconId"
        val newCategory = Category(
            name = binding.etCategoryName.text.toString() ,
            secondAmount = limit ,
            iconId = iconId,
            firstAmount = 0,
            docName = docName,
            currency = prefs.getSettingsCurrency()
        )
        usersCollection.document(prefs.getCurrentUserId())
            .collection(colName).document(docName).set(newCategory)
        dialog?.dismiss()
        if (isSpending) updater.updateSpendingCard()
        else updater.updateEarnings()
    }

    private fun setupTextLimitations() {
        binding.etCategoryName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence ,
                start: Int ,
                count: Int ,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence , start: Int , before: Int , count: Int) {
                val length: Int = binding.etCategoryName.length()
                val convert = length.toString()
                binding.maxText.text = "max: $convert/9"
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.etLimit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence , start: Int , before: Int , count: Int) {
                val length: Int = binding.etLimit.length()
                val convert = length.toString()
                binding.maxLimit.text = "max: $convert/5"
            }
            override fun afterTextChanged(s: Editable) {
                if (binding.etLimit.text.toString().startsWith("0")
                    && binding.etLimit.text.length > 1) {
                    binding.etLimit.setText("0")
                    binding.etLimit.setSelection(binding.etLimit.text.toString().length)
                }
            }
        })
    }

    override fun chosenIcon(item: CategoryName) {
        binding.icon.setImageResource(item.name.toInt())
        iconId = item.id
    }
}
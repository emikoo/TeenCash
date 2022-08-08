package com.teenteen.teencash.presentation.ui.fragments.main.home.bottom_sheets

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.teencash.ui.bottom_sheet.icon.IconListBS
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.CategoryName
import com.teenteen.teencash.databinding.BsAddCategoryBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.extensions.show
import com.teenteen.teencash.presentation.interfaces.PickerItem
import com.teenteen.teencash.presentation.interfaces.UpdateData

class AddCategoryBS(private val updater: UpdateData) :
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

    var categoryName = ""
    var limit = 0
    var iconId = 0

    override fun setupViews() {
        setupTextLimitations()
        setupListeners()
    }

    private fun setupListeners() {
        binding.ibEdit.setOnClickListener {
            IconListBS(this).show(activity?.supportFragmentManager)
        }
        binding.btnAdd.setOnClickListener {
            categoryName = binding.etCategoryName.text.toString()
            limit = binding.etLimit.text.toString().toInt()
            checkFields()
        }
        binding.ibClose.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun checkFields() {
        if (binding.etCategoryName.text.isNotBlank() && binding.etCategoryName.text.isNotEmpty()
            && binding.etLimit.text.isNotBlank() && binding.etLimit.text.isNotEmpty() && iconId != null
        ) {
            checkIfDocExists()
        } else Toast.makeText(requireContext() , getString(R.string.check_all_data) , Toast.LENGTH_LONG).show()
    }

    private fun checkIfDocExists() {
        val categoriesOfUser = usersCollection.document(prefs.getCurrentUserId())
            .collection("categories").document(categoryName)
        categoriesOfUser.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                    if (document.exists()) {
                        Toast.makeText(requireContext(), getString(R.string.item_exists), Toast.LENGTH_LONG).show()
                    } else {
                        addCategory()
                    }
                }
            }
        }
    }

    private fun addCategory() {
        val newCategory = Category(
            name = categoryName ,
            secondAmount = limit ,
            iconId = iconId
        )
        dialog?.dismiss()
        updater.updateCategory()
        usersCollection.document(prefs.getCurrentUserId())
            .collection("categories").document(categoryName).set(newCategory)
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
            override fun beforeTextChanged(
                s: CharSequence ,
                start: Int ,
                count: Int ,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence , start: Int , before: Int , count: Int) {
                val length: Int = binding.etLimit.length()
                val convert = length.toString()
                binding.maxLimit.text = "max: $convert/4"
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun chosenIcon(item: CategoryName) {
        binding.icon.setBackgroundResource(item.name.toInt())
        iconId = item.id
    }
}
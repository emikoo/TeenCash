package com.teenteen.teencash.presentation.ui.common_bottom_sheets

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.firestore.FirebaseFirestore
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.Debtor
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.data.model.InfoStatistic
import com.teenteen.teencash.databinding.BsAddBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.extensions.*
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.utills.AddBottomSheetKeys
import com.teenteen.teencash.presentation.utills.checkInternetConnection
import com.teenteen.teencash.presentation.utills.convertAmount
import com.teenteen.teencash.view_model.MainViewModel

class BottomSheetAdd(
    private val updater: UpdateData , private val key: AddBottomSheetKeys , private val itemCategory: Category? = null ,
    val itemDebtor: Debtor? = null) :
    BaseBottomSheetDialogFragment<BsAddBinding>() {

    lateinit var viewModel: MainViewModel
    var categoryName = ""
    var balance = 0
    var limit = 0
    private var savedMoney = 0
    var spentToday = 0

    override fun setupViews() {
        viewModel = MainViewModel()
        checkInternetConnection(this::subscribeToLiveData, requireContext())
        when (key) {
            AddBottomSheetKeys.ADD_PIGGY_BANK , AddBottomSheetKeys.CREATE_MOTHERFUCKER ,
            AddBottomSheetKeys.CREATE_BLOODSUCKER -> {
                binding.title.isInvisible()
                binding.limit.isInvisible()
                binding.etName.isVisible()
                binding.etLimit.isGone()
                binding.tvCurrency1.isGone()
                binding.spinner.isVisible()
                binding.ivSpinner.isVisible()
                setupSpinner()
            }
            AddBottomSheetKeys.UPDATE_BLOODSUCKER ,
            AddBottomSheetKeys.UPDATE_MOTHERFUCKER -> {
                binding.title.isInvisible()
                binding.limit.isInvisible()
                binding.etName.isVisible()
                binding.etLimit.isGone()
                binding.tvCurrency1.isGone()
                binding.spinner.isGone()
                binding.ivSpinner.isGone()
            }
            AddBottomSheetKeys.UPDATE_PIGGY , AddBottomSheetKeys.UPDATE_CATEGORY -> {
                binding.title.isInvisible()
                binding.limit.isInvisible()
                binding.etName.isVisible()
                binding.etLimit.isVisible()
                binding.tvCurrency1.isVisible()
                binding.spinner.isGone()
                binding.ivSpinner.isGone()
            }
            else -> {
                binding.title.isVisible()
                binding.limit.isVisible()
                binding.etName.isInvisible()
                binding.etLimit.isGone()
                binding.tvCurrency1.isGone()
                binding.spinner.isGone()
                binding.ivSpinner.isGone()
            }
        }
        setupTitlesByKey()
        checkInternetConnection(this::setupListener, requireContext())
        binding.btnCancel.setOnClickListener { dialog !!.dismiss() }
        setupListener()
    }

    private fun setupSpinner() {
        val adapter =
            ArrayAdapter.createFromResource(requireActivity(), R.array.spinner_currency , R.layout.spinner_currency)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_currency)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>? , p1: View? , p2: Int , p3: Long) {
                binding.tvCurrency.text = binding.spinner.selectedItem.toString()
            }
        }
    }

    private fun setupTitlesByKey() {
        when (key) {
            AddBottomSheetKeys.ADD_PIGGY_BANK -> setupTextByKey(etAmountHint = resources.getString(R.string.goal) , btnAddText = false)
            AddBottomSheetKeys.SET_LIMIT -> {
                setupTextByKey(title = getString(R.string.your_daily_limit),
                    etAmountHint = resources.getString(R.string.limit) , btnAddText = false)
                binding.tvCurrency.text = prefs.getSettingsCurrency()
            }
            AddBottomSheetKeys.SPENT_CATEGORY -> {
                setupTextByKey(title = itemCategory !!.name, etAmountHint = resources.getString(R.string.spent) , btnAddText = true)
                val limit = resources.getString(R.string.limit)
                binding.limit.text = "$limit: ${itemCategory.firstAmount}/${itemCategory.secondAmount}"
                binding.tvCurrency.text = itemCategory.currency
            }
            AddBottomSheetKeys.SAVED_PIGGY -> {
                setupTextByKey(title = itemCategory !!.name,
                    etAmountHint = "0" , btnAddText = false)
                binding.tvCurrency.text = itemCategory.currency
            }
            AddBottomSheetKeys.CURRENT_BALANCE -> {
                setupTextByKey(title = getString(R.string.Balance),
                    etAmountHint = "0" , btnAddText = true)
                binding.tvCurrency.text = prefs.getSettingsCurrency()
            }
            AddBottomSheetKeys.CREATE_MOTHERFUCKER -> setupTextByKey(etAmountHint = "0" , btnAddText = false)
            AddBottomSheetKeys.CREATE_BLOODSUCKER -> setupTextByKey(etAmountHint = "0" , btnAddText = false)
            AddBottomSheetKeys.UPDATE_MOTHERFUCKER -> setupUpdateTexts(true)
            AddBottomSheetKeys.UPDATE_BLOODSUCKER -> setupUpdateTexts(true)
            AddBottomSheetKeys.UPDATE_CATEGORY -> setupUpdateTexts(false)
            AddBottomSheetKeys.UPDATE_PIGGY -> setupUpdateTexts(false)
            AddBottomSheetKeys.UPDATE_BALANCE -> {
                binding.title.text = getString(R.string.Balance)
                binding.btnAdd.text = resources.getString(R.string.save)
                binding.tvCurrency.text = prefs.getSettingsCurrency()
            }
        }
    }

    private fun setupTextByKey(title: String? = null, etAmountHint: String , btnAddText: Boolean) {
        binding.title.text = title
        binding.etAmount.hint = etAmountHint
        binding.etAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence? , p1: Int , p2: Int , p3: Int) {}
            override fun onTextChanged(p0: CharSequence? , p1: Int , p2: Int , p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (binding.etAmount.text.toString().startsWith("0")
                    && binding.etAmount.text.length > 1) {
                    binding.etAmount.setText("0")
                    binding.etAmount.setSelection(binding.etAmount.text.toString().length)
                }
            }
        })
        if (btnAddText) binding.btnAdd.text = resources.getString(R.string.add)
        else binding.btnAdd.text = resources.getString(R.string.save)
    }

    private fun setupUpdateTexts(isDebtor: Boolean) {
        if (isDebtor) {
            binding.etName.setText(itemDebtor?.name)
            binding.etAmount.setText("${itemDebtor?.amount}")
            binding.tvCurrency.text = "${itemDebtor?.currency}"
        } else {
            binding.etName.setText(itemCategory?.name)
            binding.etAmount.setText("${itemCategory?.firstAmount}")
            binding.etLimit.setText("${itemCategory?.secondAmount}")
            binding.tvCurrency.text = "${itemCategory?.currency}"
            binding.tvCurrency1.text = "${itemCategory?.currency}"
        }
        binding.etName.setSelection(binding.etName.length())
        binding.etAmount.setSelection(binding.etAmount.length())
        binding.etLimit.setSelection(binding.etLimit.length())
        binding.btnAdd.text = resources.getString(R.string.save)
    }

    private fun setupListener() {
        binding.btnAdd.setOnClickListener {
            when (key) {
                AddBottomSheetKeys.ADD_PIGGY_BANK -> {
                    if (binding.etName.text.isNotBlank() && binding.etName.text.isNotEmpty()
                        && binding.etAmount.text.isNotBlank() && binding.etAmount.text.isNotEmpty()
                    ) {
                        categoryName = binding.etName.text.toString()
                        limit = binding.etAmount.text.toString().toInt()
                        checkIfDocExists(
                            "piggy_banks" , categoryName , whenDocNotExistAction = this::addPiggy ,
                            whenDocExistAction = this::toastItemExists
                        )
                    } else Toast.makeText(
                        requireContext() ,
                        getString(R.string.check_all_data) ,
                        Toast.LENGTH_LONG
                    ).show()
                }
                AddBottomSheetKeys.SET_LIMIT -> checkField(this::updateLimit)
                AddBottomSheetKeys.SPENT_CATEGORY -> checkField(this::spendMoney)
                AddBottomSheetKeys.SAVED_PIGGY -> checkIfDocExists(
                    "statistics" ,
                    "info" ,
                    whenDocExistAction = this::saveMoney ,
                    whenDocNotExistAction = this::createInfoDoc
                )
                AddBottomSheetKeys.CURRENT_BALANCE -> checkIfDocExists(
                    "statistics" ,
                    "info" ,
                    whenDocExistAction = this::addBalance ,
                    whenDocNotExistAction = this::createInfoDoc
                )
                AddBottomSheetKeys.CREATE_MOTHERFUCKER -> if (binding.etName.text.isNotBlank()) checkIfDocExists(
                    "motherfuckers" ,
                    binding.etName.text.toString() ,
                    whenDocExistAction = this::toastItemExists ,
                    whenDocNotExistAction = this::createMFDoc
                )
                else Toast.makeText(
                    requireContext() ,
                    getString(R.string.check_all_data) ,
                    Toast.LENGTH_LONG
                ).show()
                AddBottomSheetKeys.CREATE_BLOODSUCKER -> if (binding.etName.text.isNotBlank()) checkIfDocExists(
                    "bloodsuckers" ,
                    binding.etName.text.toString() ,
                    whenDocExistAction = this::toastItemExists ,
                    whenDocNotExistAction = this::createBSDoc
                )
                else Toast.makeText(
                    requireContext() ,
                    getString(R.string.check_all_data) ,
                    Toast.LENGTH_LONG
                ).show()
                AddBottomSheetKeys.UPDATE_MOTHERFUCKER -> if (binding.etName.text.isNotBlank()
                    && binding.etAmount.text.isNotBlank()
                ) updateMotherfucker()
                else Toast.makeText(
                    requireContext() ,
                    getString(R.string.check_all_data) ,
                    Toast.LENGTH_SHORT
                ).show()
                AddBottomSheetKeys.UPDATE_BLOODSUCKER -> if (binding.etName.text.isNotBlank()
                    && binding.etAmount.text.isNotBlank()
                ) updateBloodsucker()
                else Toast.makeText(
                    requireContext() ,
                    getString(R.string.check_all_data) ,
                    Toast.LENGTH_SHORT
                ).show()
                AddBottomSheetKeys.UPDATE_CATEGORY -> updateCategory()
                AddBottomSheetKeys.UPDATE_PIGGY -> updatePiggy()
                AddBottomSheetKeys.UPDATE_BALANCE -> checkField(this::updateBalance)
            }
        }
    }

    private fun addPiggy() {
        val newGoal = Category(
            name = categoryName ,
            secondAmount = limit ,
            iconId = 777 ,
            firstAmount = 0,
            docName = "$categoryName$limit",
            currency = binding.tvCurrency.text.toString()
        )
        dialog?.dismiss()
        updater.updatePiggyBank()
        usersCollection.document(prefs.getCurrentUserId())
            .collection("piggy_banks").document("$categoryName$limit").set(newGoal)
    }

    private fun updateLimit() {
        viewModel.updateLimit(
            prefs.getCurrentUserId() ,
            binding.etAmount.text.toString().toInt()
        )
        updater.updateStatistics()
    }

    private fun spendMoney() {
        val spentAmount = binding.etAmount.text.toString().toInt()
        if (spentAmount != 0) {
            val before = itemCategory !!.firstAmount
            val value = before + spentAmount
            val item = History(itemCategory.name, spentAmount, true, getCurrentDate(),
                getCurrentDateTime(), getCurrentMonth(), itemCategory.iconId, itemCategory.currency.toString())
            viewModel.spendCategory(prefs.getCurrentUserId() , itemCategory.docName, value)
            viewModel.updateBalance(prefs.getCurrentUserId() , balance - spentAmount.convertAmount
                (prefs.getSettingsCurrency(), binding.tvCurrency.text.toString()))
            viewModel.updateSpentAmount(prefs.getCurrentUserId() , spentToday + spentAmount.convertAmount
                (prefs.getSettingsCurrency(), binding.tvCurrency.text.toString()))
            viewModel.putToHistory(prefs.getCurrentUserId(), item)
            updater.updateCategory()
        }
    }

    private fun saveMoney() {
        val input = binding.etAmount.text.toString().toInt()
        if (input != 0) {
            val total = input + itemCategory !!.firstAmount
            val item = History(itemCategory.name, input, true, getCurrentDate(),
                getCurrentDateTime(), getCurrentMonth(),777, itemCategory.currency.toString())
            viewModel.updateSavedAmount(prefs.getCurrentUserId() , savedMoney+input.convertAmount
                (prefs.getSettingsCurrency(), binding.tvCurrency.text.toString()))
            viewModel.saveMoneyPiggy(prefs.getCurrentUserId() , itemCategory.docName , total)
            viewModel.updateBalance(prefs.getCurrentUserId(), balance-input.convertAmount
                (prefs.getSettingsCurrency(), binding.tvCurrency.text.toString()))
            viewModel.putToHistory(prefs.getCurrentUserId(), item)
            updater.updatePiggyBank()
        }
    }

    private fun addBalance() {
        if (binding.etAmount.text.toString().toInt() != 0) {
            val newBalance = balance + binding.etAmount.text.toString().toInt()
            val item = History("Balance", binding.etAmount.text.toString().toInt(), false, getCurrentDate(),
                getCurrentDateTime(), getCurrentMonth(),1313, binding.tvCurrency.text.toString())
            viewModel.putToHistory(prefs.getCurrentUserId(), item)
            viewModel.updateBalance(prefs.getCurrentUserId() , newBalance)
            updater.updateStatistics()
        }
    }

    private fun updateBalance() {
        viewModel.updateBalance(prefs.getCurrentUserId(), binding.etAmount.text.toString().toInt())
        updater.updateStatistics()
        dialog?.dismiss()
    }

    private fun createInfoDoc() {
        val defaultInfo: InfoStatistic = InfoStatistic(0 , "KGD",0 , 0 , 0)
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(prefs.getCurrentUserId())
            .collection("statistics").document("info").set(defaultInfo)
        addBalance()
    }

    private fun createMFDoc() {
        val name = binding.etName.text.toString()

        if (name.isNotBlank()) {
            val amount = binding.etAmount.text.toString().toInt()
            val docName = "$name$amount"
            val currentCurrency = binding.tvCurrency.text.toString()
            val convertedAmount = amount.convertAmount(prefs.getSettingsCurrency(), currentCurrency)
            val newBalance = balance - convertedAmount
            val default: Debtor = Debtor("$name$amount", name, amount, currentCurrency)
            val item = History(name, amount, true, getCurrentDate(), getCurrentDateTime(),
                getCurrentMonth(),666, currentCurrency)
            viewModel.putToHistory(prefs.getCurrentUserId(), item)
            viewModel.createMotherfucker(prefs.getCurrentUserId(), docName, default)
            viewModel.updateBalance(prefs.getCurrentUserId(), newBalance)
            updater.updateMFList()
        } else Toast.makeText(requireContext(), getString(R.string.enter_the_name), Toast.LENGTH_SHORT).show()
    }

    private fun createBSDoc() {
        val name = binding.etName.text.toString()
        val amount = binding.etAmount.text.toString().toInt()
        val docName = "$name$amount"
        val currentCurrency = binding.tvCurrency.text.toString()
        val convertedAmount = amount.convertAmount(prefs.getSettingsCurrency(), currentCurrency)
        val newBalance = balance + convertedAmount
        val default: Debtor = Debtor("$name$amount", name, amount, currentCurrency)
        val item = History(name, amount, false, getCurrentDate(), getCurrentDateTime(),
            getCurrentMonth(),666, currentCurrency)
        viewModel.putToHistory(prefs.getCurrentUserId(), item)
        viewModel.createBloodsucker(prefs.getCurrentUserId(), docName, default)
        viewModel.updateBalance(prefs.getCurrentUserId(), newBalance)
        updater.updateBSList()
    }

    private fun updateMotherfucker() {
        itemDebtor?.let {
            val amount = binding.etAmount.text.toString().toInt()
            val convertedAmount = amount.convertAmount(prefs.getSettingsCurrency(), binding.tvCurrency.text.toString())
            val gap = it.amount - convertedAmount
            if (binding.etAmount.text.toString().toInt() != 0 || gap !=0) {
                val newBalance = balance + gap
                val item = History(it.name, gap, false, getCurrentDate(),
                    getCurrentDateTime(), getCurrentMonth(),666, binding.tvCurrency.text.toString())
                viewModel.putToHistory(prefs.getCurrentUserId(), item)
                viewModel.updateBalance(prefs.getCurrentUserId(), newBalance)
            }
            viewModel.updateMotherfucker(prefs.getCurrentUserId(), it.docName, binding.etName.text.toString(),
                binding.etAmount.text.toString().toInt())
            updater.updateMFList()
            dialog?.dismiss()
        }
    }

    private fun updateBloodsucker() {
        itemDebtor?.let {
            val amount = binding.etAmount.text.toString().toInt()
            val convertedAmount = amount.convertAmount(prefs.getSettingsCurrency(), binding.tvCurrency.text.toString())
            val gap = it.amount - convertedAmount
            if (binding.etAmount.text.toString().toInt() != 0 || gap !=0) {
                val newBalance = balance - gap
                val item = History(it.name, gap, true, getCurrentDate(),
                    getCurrentDateTime(), getCurrentMonth(),666, binding.tvCurrency.text.toString())
                viewModel.putToHistory(prefs.getCurrentUserId(), item)
                viewModel.updateBalance(prefs.getCurrentUserId(), newBalance)
            }
            viewModel.updateBloodsucker(prefs.getCurrentUserId(), it.docName, binding.etName.text.toString(),
                binding.etAmount.text.toString().toInt())
            updater.updateBSList()
            dialog?.dismiss()
        }
    }

    private fun updateCategory() {
        itemCategory?.let {
            val name = binding.etName.text.toString()
            val limit = binding.etLimit.text.toString()
            val amount = binding.etAmount.text.toString()
            val convertedAmount = amount.toInt().convertAmount(prefs.getSettingsCurrency(), binding.tvCurrency.text.toString())
            val gap = it.firstAmount-convertedAmount
            if (name.isNotBlank() && limit.isNotBlank() && amount.isNotBlank()) {
                viewModel.updateCategory(prefs.getCurrentUserId(), it.docName, name, amount.toInt(), limit.toInt())
                if (amount.toInt() != 0) {
                    val isSpent = gap < 0
                    val historyAmount = if (gap < 0) amount.toInt()-it.firstAmount
                    else gap
                    viewModel.putToHistory(prefs.getCurrentUserId(), History(name, historyAmount, isSpent,
                        getCurrentDate(), getCurrentDateTime(), getCurrentMonth(), it.iconId, binding.tvCurrency.text.toString()))
                    viewModel.updateSpentAmount(prefs.getCurrentUserId(), spentToday-gap)
                    viewModel.updateBalance(prefs.getCurrentUserId(), balance+gap)
                }
                updater.updateCategory()
                dialog?.dismiss()
            } else Toast.makeText(
                requireContext() ,
                getString(R.string.check_all_data) ,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updatePiggy() {
        itemCategory?.let {
            val name = binding.etName.text.toString()
            val limit = binding.etLimit.text.toString()
            val amount = binding.etAmount.text.toString()
            val convertedAmount = amount.toInt().convertAmount(prefs.getSettingsCurrency(), binding.tvCurrency.text.toString())
            val gap = it.firstAmount-convertedAmount
            if (name.isNotBlank() && limit.isNotBlank() && amount.isNotBlank()) {
                viewModel.updatePiggy(prefs.getCurrentUserId(), it.docName, name, amount.toInt(), limit.toInt())
                if (amount.toInt() != 0) {
                    val isSpent = gap < 0
                    val historyAmount = if (gap < 0) amount.toInt()-it.firstAmount
                    else gap
                    viewModel.putToHistory(prefs.getCurrentUserId(), History(name, historyAmount, isSpent,
                        getCurrentDate(), getCurrentDateTime(), getCurrentMonth(), it.iconId, binding.tvCurrency.text.toString()))
                    viewModel.updateSavedAmount(prefs.getCurrentUserId(), savedMoney-gap)
                    viewModel.updateBalance(prefs.getCurrentUserId(), balance+gap)
                }
                updater.updatePiggyBank()
                dialog?.dismiss()
            } else Toast.makeText(
                requireContext() ,
                getString(R.string.check_all_data) ,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkField(action: () -> Unit) {
        if (binding.etAmount.text.isNotBlank()) {
            action()
            dialog?.dismiss()
        } else Toast.makeText(
            requireContext() ,
            getString(R.string.check_all_data) ,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun toastItemExists() {
        Toast.makeText(requireContext() , getString(R.string.item_exists) , Toast.LENGTH_LONG)
            .show()
    }

    private fun checkIfDocExists(
        colName: String , docName: String ,
        whenDocExistAction: () -> Unit ,
        whenDocNotExistAction: () -> Unit
    ) {
        if (binding.etAmount.text.isNotBlank()) {
            val categoriesOfUser = usersCollection.document(prefs.getCurrentUserId())
                .collection(colName).document(docName)
            categoriesOfUser.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        if (document.exists()) {
                            whenDocExistAction()
                        } else {
                            whenDocNotExistAction()
                        }
                    }
                }
            }
            dialog?.dismiss()
        } else Toast.makeText(
            requireContext() ,
            getString(R.string.check_all_data) ,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun attachBinding(
        list: MutableList<BsAddBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsAddBinding.inflate(layoutInflater , container , attachToRoot))
    }

    private fun subscribeToLiveData() {
        viewModel.getBalance(prefs.getCurrentUserId())
        viewModel.balance.observe(viewLifecycleOwner, Observer {
            balance = it
            if (key == AddBottomSheetKeys.UPDATE_BALANCE) {
                binding.etAmount.setText("$it")
                binding.etAmount.setSelection(binding.etAmount.length())
            }
        })
        viewModel.getSavedAmount(prefs.getCurrentUserId())
        viewModel.saved.observe(viewLifecycleOwner) { savedMoney = it }
        viewModel.getSpentAmount(prefs.getCurrentUserId())
        viewModel.spentAmount.observe(viewLifecycleOwner) { spentToday = it }
    }
}

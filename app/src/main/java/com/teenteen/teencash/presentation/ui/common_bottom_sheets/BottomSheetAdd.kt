package com.teenteen.teencash.presentation.ui.common_bottom_sheets

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.Debtor
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.data.model.InfoStatistic
import com.teenteen.teencash.databinding.BsAddBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.extensions.*
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.utills.*
import com.teenteen.teencash.view_model.MainViewModel

class BottomSheetAdd(
    private val updater: UpdateData , private val key: AddBottomSheetKeys , private val itemCategory: Category? = null ,
    val itemDebtor: Debtor? = null) :
    BaseBottomSheetDialogFragment<BsAddBinding>() {

    lateinit var viewModel: MainViewModel
    var balance = 0
    private var savedMoney = 0
    var spentToday = 0

    override fun attachBinding(list: MutableList<BsAddBinding>, layoutInflater: LayoutInflater,
        container: ViewGroup?, attachToRoot: Boolean){
        list.add(BsAddBinding.inflate(layoutInflater , container , attachToRoot))}

    override fun setupViews() {
        viewModel = MainViewModel()
        checkInternetConnection(this::subscribeToLiveData, requireContext())
        when (key) {
            AddBottomSheetKeys.CREATE_PIGGY , AddBottomSheetKeys.CREATE_MOTHERFUCKER ,
            AddBottomSheetKeys.CREATE_BLOODSUCKER, AddBottomSheetKeys.UPDATE_BLOODSUCKER ,
            AddBottomSheetKeys.UPDATE_MOTHERFUCKER -> {
                binding.title.isInvisible()
                binding.limit.isInvisible()
                binding.etName.isVisible()
                binding.etLimit.isGone()
            }
            AddBottomSheetKeys.UPDATE_PIGGY , AddBottomSheetKeys.UPDATE_SPENDING_CARD -> {
                binding.title.isInvisible()
                binding.limit.isInvisible()
                binding.etName.isVisible()
                binding.etLimit.isVisible()
            }
            else -> {
                binding.title.isVisible()
                binding.limit.isVisible()
                binding.etName.isInvisible()
                binding.etLimit.isGone()
            }
        }
        setupTitlesByKey()
        checkInternetConnection(this::setupListener, requireContext())
        setupListener()
    }

    private fun setupTitlesByKey() {
        binding.tvCurrency.text = prefs.getSettingsCurrency()
        when (key) {
            AddBottomSheetKeys.CREATE_PIGGY -> setText(etAmountHint = resources.getString(R.string.goal) , btnAddText = false)
            AddBottomSheetKeys.SET_LIMIT -> setText(title = getString(R.string.your_daily_limit),
                etAmountHint = resources.getString(R.string.limit) , btnAddText = false)
            AddBottomSheetKeys.SPENT_CARD -> {
                setText(title = itemCategory!!.name, etAmountHint = resources.getString(R.string.spent) , btnAddText = true)
                val limit = resources.getString(R.string.limit)
                binding.limit.text = "$limit: ${itemCategory.firstAmount}/${itemCategory.secondAmount}"
                binding.tvCurrency.text = itemCategory.currency
            }
            AddBottomSheetKeys.ADD_MONEY_TO_PIGGY -> {
                setText(title = itemCategory !!.name,
                    etAmountHint = "0" , btnAddText = false)
                binding.tvCurrency.text = itemCategory.currency
            }
            AddBottomSheetKeys.ADD_BALANCE -> setText(title = getString(R.string.Balance),
                    etAmountHint = "0" , btnAddText = true)
            AddBottomSheetKeys.CREATE_MOTHERFUCKER -> setText(etAmountHint = "0" , btnAddText = false)
            AddBottomSheetKeys.CREATE_BLOODSUCKER -> setText(etAmountHint = "0" , btnAddText = false)
            AddBottomSheetKeys.UPDATE_MOTHERFUCKER -> updateText(true)
            AddBottomSheetKeys.UPDATE_BLOODSUCKER -> updateText(true)
            AddBottomSheetKeys.UPDATE_SPENDING_CARD -> updateText(false)
            AddBottomSheetKeys.UPDATE_PIGGY -> updateText(false)
            AddBottomSheetKeys.UPDATE_BALANCE -> setText(title = getString(R.string.Balance),
                etAmountHint = "0", btnAddText = false)
        }
    }

    private fun setText(title: String? = null, etAmountHint: String , btnAddText: Boolean) {
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

    private fun updateText(isDebtor: Boolean) {
        if (isDebtor) {
            binding.etName.setText(itemDebtor?.name)
            binding.etAmount.setText("${itemDebtor?.amount}")
            binding.tvCurrency.text = "${itemDebtor?.currency}"
        } else {
            binding.etName.setText(itemCategory?.name)
            binding.etAmount.setText("${itemCategory?.firstAmount}")
            binding.etLimit.setText("${itemCategory?.secondAmount}")
            binding.tvCurrency.text = "${itemCategory?.currency}"
        }
        binding.etName.setSelection(binding.etName.length())
        binding.etAmount.setSelection(binding.etAmount.length())
        binding.etLimit.setSelection(binding.etLimit.length())
        binding.btnAdd.text = resources.getString(R.string.save)
    }

    private fun setupListener() {
        binding.btnCancel.setOnClickListener { dialog !!.dismiss() }
        binding.btnAdd.setOnClickListener {
            when (key) {
                AddBottomSheetKeys.CREATE_PIGGY -> checkField(this, requireContext(),
                    binding.etName, binding.etAmount, this::checkIfDocExists)
                AddBottomSheetKeys.CREATE_MOTHERFUCKER -> checkField(this, requireContext(),
                    binding.etName, binding.etAmount, this::checkIfDocExists)
                AddBottomSheetKeys.CREATE_BLOODSUCKER -> checkField(this, requireContext(),
                    binding.etName, binding.etAmount, this::checkIfDocExists)
                AddBottomSheetKeys.UPDATE_MOTHERFUCKER -> checkField(this, requireContext(),
                    binding.etAmount, binding.etName, this::updateDebtor)
                AddBottomSheetKeys.UPDATE_BLOODSUCKER -> checkField(this, requireContext(),
                    binding.etAmount, binding.etName, this::updateDebtor)
                AddBottomSheetKeys.UPDATE_SPENDING_CARD -> updateCategory()
                AddBottomSheetKeys.UPDATE_PIGGY -> updateCategory()
                AddBottomSheetKeys.UPDATE_BALANCE -> checkField(this, requireContext(),
                    binding.etAmount, action = this::updateBalance)
                AddBottomSheetKeys.SET_LIMIT -> checkField(this, requireContext(),
                    binding.etAmount, action = this::updateLimit)
                AddBottomSheetKeys.SPENT_CARD -> checkField(this, requireContext(),
                    binding.etAmount, action = this::spendMoney)
                AddBottomSheetKeys.ADD_MONEY_TO_PIGGY -> checkField(this, requireContext(),
                    binding.etAmount, action = this::checkIfDocExists)
                AddBottomSheetKeys.ADD_BALANCE -> checkField(this, requireContext(),
                    binding.etAmount, action = this::checkIfDocExists)
            }
        }
    }

    private fun checkIfDocExists(){
        var docName = key.docName
        if (docName == null) docName = binding.etName.text.toString()
        val categoriesOfUser = key.colName?.let {
            usersCollection.document(prefs.getCurrentUserId()).collection(it).document(docName) }
        categoriesOfUser?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        when (key) {
                            AddBottomSheetKeys.ADD_MONEY_TO_PIGGY -> {saveMoney()}
                            AddBottomSheetKeys.ADD_BALANCE -> {addBalance()}
                            else -> requireContext().showToast(getString(R.string.item_exists))
                        }
                    }
                    else {
                        if (key.colName == "statistics" && key.docName == "info") {
                            val defaultInfo: InfoStatistic = InfoStatistic(0 ,
                                prefs.getSettingsCurrency(),0 , 0 , 0)
                            db.collection("users")
                                .document(prefs.getCurrentUserId()).collection(key.colName!!)
                                .document(key.docName!!).set(defaultInfo)
                            addBalance()
                        }
                        else if (key == AddBottomSheetKeys.CREATE_PIGGY) addPiggy()
                        else {
                            val amount = binding.etAmount.text.toString().toInt()
                            var isSpent = true
                            val currentCurrency = binding.tvCurrency.text.toString()
                            val convertedAmount = amount.convertAmount(prefs.getSettingsCurrency(), currentCurrency)
                            val default: Debtor = Debtor("$docName$amount", docName, amount, currentCurrency)
                            if (key == AddBottomSheetKeys.CREATE_MOTHERFUCKER) {
                                isSpent = true
                                viewModel.createMotherfucker(prefs.getCurrentUserId(), "$docName$amount", default)
                                viewModel.updateBalance(prefs.getCurrentUserId(), balance - convertedAmount)
                                viewModel.updateSpentAmount(prefs.getCurrentUserId(), spentToday+amount)
                                updater.updateMFList()
                            } else if (key == AddBottomSheetKeys.CREATE_BLOODSUCKER) {
                                isSpent = false
                                viewModel.createBloodsucker(prefs.getCurrentUserId(), "$docName$amount", default)
                                viewModel.updateBalance(prefs.getCurrentUserId(), balance + convertedAmount)
                                updater.updateBSList()
                            }
                            val item = History(docName, amount, isSpent, getCurrentDate(), getCurrentDateTime(),
                                getCurrentMonth(), key.imageID!!, currentCurrency)
                            viewModel.putToHistory(prefs.getCurrentUserId(), item)
                        }
                    }
                }
            }
        }
        dialog?.dismiss()
    }

    private fun updateDebtor() {
        itemDebtor?.let {
            val amount = binding.etAmount.text.toString().toInt()
            val convertedAmount = amount.convertAmount(prefs.getSettingsCurrency(), binding.tvCurrency.text.toString())
            var gap = it.amount - convertedAmount
            var isSpent = true
            if (binding.etAmount.text.toString().toInt() != 0 && gap !=0) {
                if (key == AddBottomSheetKeys.UPDATE_MOTHERFUCKER) {
                    isSpent = amount > itemDebtor.amount
                    if (isSpent) viewModel.updateSpentAmount(prefs.getCurrentUserId(), spentToday+(amount - itemDebtor.amount))
                    viewModel.updateBalance(prefs.getCurrentUserId(), balance + gap)
                    viewModel.updateMotherfucker(prefs.getCurrentUserId(), it.docName, binding.etName.text.toString(),
                        binding.etAmount.text.toString().toInt())
                    updater.updateMFList()
                } else if (key == AddBottomSheetKeys.UPDATE_BLOODSUCKER) {
                    isSpent = amount < itemDebtor.amount
                    if (isSpent) viewModel.updateSpentAmount(prefs.getCurrentUserId(), spentToday+(itemDebtor.amount-amount))
                    viewModel.updateBalance(prefs.getCurrentUserId(), balance - gap)
                    viewModel.updateBloodsucker(prefs.getCurrentUserId(), it.docName, binding.etName.text.toString(),
                        binding.etAmount.text.toString().toInt())
                    updater.updateBSList()
                }
                if (gap < 0) gap += (gap * (- 2))
                val item = History(it.name, gap, isSpent, getCurrentDate(),
                    getCurrentDateTime(), getCurrentMonth(), key.imageID!!, binding.tvCurrency.text.toString())
                viewModel.putToHistory(prefs.getCurrentUserId(), item)
                dialog?.dismiss()
            }
        }
    }

    private fun updateCategory() {
        itemCategory?.let {
            val name = binding.etName.text.toString()
            val limit = binding.etLimit.text.toString()
            val amount = binding.etAmount.text.toString()
            val convertedAmount = amount.toInt().convertAmount(prefs.getSettingsCurrency(), binding.tvCurrency.text.toString())
            val gap = it.firstAmount-convertedAmount
            if (name.isNotBlank() && limit.isNotBlank() && amount.isNotBlank() && amount.toInt() != 0) {
                val isSpent = gap < 0
                val historyAmount = if (gap < 0) amount.toInt()-it.firstAmount
                else gap
                viewModel.putToHistory(prefs.getCurrentUserId(), History(name, historyAmount, isSpent,
                    getCurrentDate(), getCurrentDateTime(), getCurrentMonth(), it.iconId, binding.tvCurrency.text.toString()))
                viewModel.updateBalance(prefs.getCurrentUserId(), balance+gap)
                if (key == AddBottomSheetKeys.UPDATE_SPENDING_CARD) {
                    viewModel.updateCategory(prefs.getCurrentUserId(), it.docName, name, amount.toInt(), limit.toInt())
                    viewModel.updateSpentAmount(prefs.getCurrentUserId(), spentToday-gap)
                    updater.updateSpendingCard()
                }
                else if (key == AddBottomSheetKeys.UPDATE_PIGGY) {
                    viewModel.updatePiggy(prefs.getCurrentUserId(), it.docName, name, amount.toInt(), limit.toInt())
                    viewModel.updateSavedAmount(prefs.getCurrentUserId(), savedMoney-gap)
                    updater.updatePiggyBank()
                }
                dialog?.dismiss()
            } else requireContext().showToast(getString(R.string.check_all_data))
        }
    }

    private fun updateBalance() {
        var gap = balance - binding.etAmount.text.toString().toInt()
        if (gap < 0) gap += (gap * (- 2))
        else viewModel.updateSpentAmount(prefs.getCurrentUserId(), spentToday+gap)
        val item = History("Balance", gap, balance > binding.etAmount.text.toString().toInt(),
            getCurrentDate(), getCurrentDateTime(), getCurrentMonth(), key.imageID!!, binding.tvCurrency.text.toString())
        viewModel.putToHistory(prefs.getCurrentUserId(), item)
        viewModel.updateBalance(prefs.getCurrentUserId(), binding.etAmount.text.toString().toInt())
        updater.updateStatistics()
    }

    private fun updateLimit() {
        viewModel.updateLimit(prefs.getCurrentUserId(), binding.etAmount.text.toString().toInt())
        updater.updateStatistics()
    }

    private fun addPiggy() {
        val limit = binding.etAmount.text.toString().toInt()
        val categoryName = binding.etName.text.toString()
        val newGoal = Category(name = categoryName, secondAmount = limit, iconId = key.imageID!!, firstAmount = 0,
            docName = "$categoryName$limit", currency = binding.tvCurrency.text.toString())
        dialog?.dismiss()
        updater.updatePiggyBank()
        key.colName?.let { usersCollection.document(prefs.getCurrentUserId())
            .collection(it).document("$categoryName$limit").set(newGoal) }
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
            updater.updateSpendingCard()
        }
    }

    private fun saveMoney() {
        val input = binding.etAmount.text.toString().toInt()
        if (input != 0) {
            val total = input + itemCategory !!.firstAmount
            val item = History(itemCategory.name, input, true, getCurrentDate(),
                getCurrentDateTime(), getCurrentMonth(), key.imageID!!, itemCategory.currency.toString())
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
                getCurrentDateTime(), getCurrentMonth(), key.imageID!!, binding.tvCurrency.text.toString())
            viewModel.putToHistory(prefs.getCurrentUserId(), item)
            viewModel.updateBalance(prefs.getCurrentUserId() , newBalance)
            updater.updateStatistics()
        }
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
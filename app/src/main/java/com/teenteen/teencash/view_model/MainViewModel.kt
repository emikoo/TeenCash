package com.teenteen.teencash.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.Debtor
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.service.FirebaseDebtorService
import com.teenteen.teencash.service.FirebaseHistoryService
import com.teenteen.teencash.service.FirebaseHomeService
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val _category = MutableLiveData<List<Category>>()
    private val _earnings = MutableLiveData<List<Category>>()
    private val _achievement = MutableLiveData<List<Category>>()
    private val _piggy = MutableLiveData<List<Category>>()
    private val _mf = MutableLiveData<List<Debtor>>()
    private val _bs = MutableLiveData<List<Debtor>>()
    private val _balance = MutableLiveData<Int>()
    val balance: LiveData<Int> = _balance
    private val _currency = MutableLiveData<String>()
    val currency: LiveData<String> = _currency
    private val _saved = MutableLiveData<Int>()
    val saved: LiveData<Int> = _saved
    private val _limit = MutableLiveData<Int>()
    val limit: LiveData<Int> = _limit
    private val _spentAmount = MutableLiveData<Int>()
    val spentAmount: LiveData<Int> = _spentAmount
    val category: LiveData<List<Category>> = _category
    val earnings: LiveData<List<Category>> = _earnings
    val achievement: LiveData<List<Category>> = _achievement
    val piggy: LiveData<List<Category>> = _piggy
    val mf: LiveData<List<Debtor>> = _mf
    val bs: LiveData<List<Debtor>> = _bs

    fun createMotherfucker(uid: String, docName: String, debtor: Debtor) {
        viewModelScope.launch {
            FirebaseDebtorService.createMotherfucker(uid, docName, debtor)
        }
    }

    fun createBloodsucker(uid: String, docName: String, debtor: Debtor) {
        viewModelScope.launch {
            FirebaseDebtorService.createBloodsucker(uid, docName, debtor)
        }
    }

    fun createAchievement(uid: String, docName: String, item: Category) {
        viewModelScope.launch {
            FirebaseHomeService.createAchievement(uid, docName, item)
        }
    }

    fun getCategories(uid: String) {
        viewModelScope.launch {
            _category.value = FirebaseHomeService.getCategories(uid)
        }
    }


    fun getEarnings(uid: String) {
        viewModelScope.launch {
            _earnings.value = FirebaseHomeService.getEarnings(uid)
        }
    }

    fun getPiggyBanks(uid: String) {
        viewModelScope.launch {
            _piggy.value = FirebaseHomeService.getPiggyBanks(uid)
        }
    }

    fun getAchievements(uid: String) {
        viewModelScope.launch {
            _achievement.value = FirebaseHomeService.getAchievements(uid)
        }
    }

    fun getMotherfuckers(uid: String) {
        viewModelScope.launch {
            _mf.value = FirebaseDebtorService.getMotherfuckers(uid)
        }
    }

    fun getBloodsuckers(uid: String) {
        viewModelScope.launch {
            _bs.value = FirebaseDebtorService.getBloodsuckers(uid)
        }
    }

    fun getBalance(uid: String) {
        viewModelScope.launch {
            _balance.value = FirebaseHomeService.getBalance(uid)
        }
    }

    fun getCurrency(uid: String) {
        viewModelScope.launch {
            _currency.value = FirebaseHomeService.getCurrency(uid)
        }
    }

    fun getSavedAmount(uid: String) {
        viewModelScope.launch {
            _saved.value = FirebaseHomeService.getSavedAmount(uid)
        }
    }

    fun getLimit(uid: String) {
        viewModelScope.launch {
            _limit.value = FirebaseHomeService.getLimit(uid)
        }
    }

    fun getSpentAmount(uid: String) {
        viewModelScope.launch {
            _spentAmount.value = FirebaseHomeService.getSpentAmount(uid)
        }
    }

    fun spendCategory(uid: String, docName: String, value: Int){
        viewModelScope.launch {
            FirebaseHomeService.spendCategory(uid, docName, value)
        }
    }

    fun saveMoneyPiggy(uid: String, docName: String, value: Int) {
        viewModelScope.launch {
            FirebaseHomeService.saveMoneyPiggy(uid, docName, value)
        }
    }

    fun updateSpentAmount(uid: String, amount: Int) {
        viewModelScope.launch {
            FirebaseHomeService.updateSpentAmount(uid, amount)
        }
    }

    fun updateBalance(uid: String, balance: Int) {
        viewModelScope.launch {
            FirebaseHomeService.updateBalance(uid, balance)
        }
    }

    fun updateSavedAmount(uid: String, amount: Int) {
        viewModelScope.launch {
            FirebaseHomeService.updateSavedAmount(uid, amount)
        }
    }

    fun updateCurrency(uid: String, currency: String) {
        viewModelScope.launch {
            FirebaseHomeService.updateCurrency(uid, currency)
        }
    }

    fun clearAmountCategory(uid: String , docName: String) {
        viewModelScope.launch {
            FirebaseHomeService.updateCategory(uid, docName)
        }
    }

    fun updateLimit(uid: String, amount: Int) {
        viewModelScope.launch {
            FirebaseHomeService.updateLimit(uid, amount)
        }
    }

    fun updateMotherfucker(uid: String, docName: String, name: String, amount: Int) {
        viewModelScope.launch {
            FirebaseDebtorService.updateMotherfucker(uid, docName, name, amount)
        }
    }

    fun updateBloodsucker(uid: String, docName: String, name: String, amount: Int) {
        viewModelScope.launch {
            FirebaseDebtorService.updateBloodsucker(uid, docName, name, amount)
        }
    }

    fun updateCategory(uid: String , docName: String , name: String , firstAmount: Int , secondAmount: Int) {
        viewModelScope.launch {
            FirebaseHomeService.updateCategory(uid, docName, name, firstAmount, secondAmount)
        }
    }

    fun updatePiggy(uid: String , docName: String , name: String , firstAmount: Int , secondAmount: Int) {
        viewModelScope.launch {
            FirebaseHomeService.updatePiggy(uid, docName, name, firstAmount, secondAmount)
        }
    }

    fun deleteMotherfucker(uid: String, docName: String){
        viewModelScope.launch {
            FirebaseDebtorService.deleteMotherfucker(uid, docName)
        }
    }

    fun deleteBloodsucker(uid: String, docName: String){
        viewModelScope.launch {
            FirebaseDebtorService.deleteBloodsucker(uid, docName)
        }
    }

    fun deleteCategory(uid: String, docName: String){
        viewModelScope.launch {
            FirebaseHomeService.deleteCategory(uid, docName)
        }
    }

    fun deletePiggy(uid: String, docName: String){
        viewModelScope.launch {
            FirebaseHomeService.deletePiggy(uid, docName)
        }
    }

    fun deleteAchievement(uid: String, docName: String){
        viewModelScope.launch {
            FirebaseHomeService.deleteAchievement(uid, docName)
        }
    }

    fun setImage(uid: String, docName: String, image: String) {
        viewModelScope.launch {
            FirebaseHomeService.setImage(uid, docName, image)
        }
    }

    fun putToHistory(uid: String, item: History) {
        viewModelScope.launch {
            FirebaseHistoryService.putToHistory(uid, item)
        }
    }
}
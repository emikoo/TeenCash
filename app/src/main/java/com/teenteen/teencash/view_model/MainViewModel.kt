package com.teenteen.teencash.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.Debtor
import com.teenteen.teencash.service.FirebaseDebtorService
import com.teenteen.teencash.service.FirebaseHomeService
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val _category = MutableLiveData<List<Category>>()
    private val _mf = MutableLiveData<List<Debtor>>()
    private val _bs = MutableLiveData<List<Debtor>>()
    private val _balance = MutableLiveData<Int>()
    val balance: LiveData<Int> = _balance
    val category: LiveData<List<Category>> = _category
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

    fun getCategories(uid: String) {
        viewModelScope.launch {
            _category.value = FirebaseHomeService.getCategories(uid)
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


    fun updateBalance(uid: String, balance: Int) {
        viewModelScope.launch {
            FirebaseHomeService.updateBalance(uid, balance)
        }
    }

    fun clearAmountCategory(uid: String , docName: String) {
        viewModelScope.launch {
            FirebaseHomeService.updateCategory(uid, docName)
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

}
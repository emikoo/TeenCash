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
    val category: LiveData<List<Category>> = _category
    fun getCategories(uid: String) {
        viewModelScope.launch {
            _category.value = FirebaseHomeService.getCategories(uid)
        }
    }
    fun clearAmountCategory(uid: String , docName: String) {
        viewModelScope.launch {
            FirebaseHomeService.updateCategory(uid, docName)
        }
    }
}
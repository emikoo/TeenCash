package com.teenteen.teencash.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.service.FirebaseProfileService
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val _category = MutableLiveData<List<Category>>()
    private val _piggy = MutableLiveData<List<Category>>()
    val category: LiveData<List<Category>> = _category
    val piggy: LiveData<List<Category>> = _piggy

    fun getCategories(uid: String) {
        viewModelScope.launch {
            _category.value = FirebaseProfileService.getCategories(uid)
        }
    }

    fun getPiggyBanks(uid: String) {
        viewModelScope.launch {
            _piggy.value = FirebaseProfileService.getPiggyBanks(uid)
        }
    }
}
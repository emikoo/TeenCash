package com.teenteen.teencash.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.service.FirebaseProfileService
import kotlinx.coroutines.launch

class UserProfileViewModel() : ViewModel() {
    private val _category = MutableLiveData<List<Category>>()
    val category: LiveData<List<Category>> = _category

    fun getCategories(uid: String) {
        viewModelScope.launch {
            _category.value = FirebaseProfileService.getCategories(uid)
        }    }
}
package com.teenteen.teencash.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teenteen.teencash.service.FirebaseAuthService
import com.teenteen.teencash.service.FirebaseHomeService
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    fun createDefaultGoal(uid: String , name: String) {
        viewModelScope.launch {
            FirebaseAuthService.createDefaultGoal(uid , name)
        }
    }

    fun createDefaultEarning(uid: String , name: String) {
        viewModelScope.launch {
            FirebaseAuthService.createDefaultEarning(uid , name)
        }
    }

    fun createDefaultCategory(uid: String , name: String) {
        viewModelScope.launch {
            FirebaseAuthService.createDefaultCategory(uid , name)
        }
    }

    fun createInfoDoc(uid: String) {
        viewModelScope.launch {
            FirebaseAuthService.createInfoDoc(uid)
        }
    }
}
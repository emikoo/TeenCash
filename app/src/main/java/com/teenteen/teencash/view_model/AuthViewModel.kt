package com.teenteen.teencash.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teenteen.teencash.service.FirebaseAuthService
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    fun createDefaultGoal(uid: String , name: String) {
        viewModelScope.launch {
            FirebaseAuthService.createDefaultGoal(uid , name)
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
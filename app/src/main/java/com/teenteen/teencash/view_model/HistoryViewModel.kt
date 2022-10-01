package com.teenteen.teencash.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.service.FirebaseHistoryService
import kotlinx.coroutines.launch
import java.util.*

class HistoryViewModel(): ViewModel() {
    private val _history = MutableLiveData<List<History>>()
    val history: LiveData<List<History>> = _history

    fun getHistory(uid: String) {
        viewModelScope.launch {
            _history.value = FirebaseHistoryService.getHistory(uid)
        }
    }

    fun getHistoryByRange(uid: String, start: Date) {
        viewModelScope.launch {
            _history.value = FirebaseHistoryService.getHistoryByRange(uid, start)
        }
    }

    fun getHistoryByMonth(userId: String, month: Date) {
        viewModelScope.launch {
            _history.value = FirebaseHistoryService.getHistoryByMonth(userId, month)
        }
    }

    fun getHistoryToday(uid: String, date: Date) {
        viewModelScope.launch {
            _history.value = FirebaseHistoryService.getHistoryToday(uid, date)
        }
    }
}
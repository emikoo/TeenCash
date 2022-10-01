package com.teenteen.teencash.service

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.data.model.History.Companion.toHistory
import com.teenteen.teencash.presentation.extensions.getCurrentDate
import kotlinx.coroutines.tasks.await
import java.util.*

object FirebaseHistoryService {
    private const val TAG = "FirebaseHistoryService"
    private val db = FirebaseFirestore.getInstance()

    fun putToHistory(uid: String, item: History) {
        try {
            FirebaseHomeService.db.collection("users").document(uid).collection("history")
                .document().set(item)
        } catch (e: Exception) {
            Log.e(TAG , "Error creating history" , e)
            FirebaseCrashlytics.getInstance().log("Error creating history")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    suspend fun getHistory(userId: String): List<History> {
        return try {
            val array = db.collection("users")
                .document(userId)
                .collection("history").orderBy("time")
            array.get().await()
                .documents.mapNotNull { it.toHistory() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting history" , e)
            FirebaseCrashlytics.getInstance().log("Error getting history")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getHistoryToday(userId: String , date: Date): List<History> {
        return try {
            val array = db.collection("users")
                .document(userId)
                .collection("history").whereEqualTo("date" , date)
            array.get().await()
                .documents.mapNotNull { it.toHistory() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting history" , e)
            FirebaseCrashlytics.getInstance().log("Error getting history")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getHistoryByRange(userId: String, start: Date): List<History> {
        val end = getCurrentDate()
        return try {
            val array = db.collection("users")
                .document(userId)
                .collection("history").whereGreaterThanOrEqualTo("date" , start).whereLessThanOrEqualTo("date" , end)
            array.get().await()
                .documents.mapNotNull { it.toHistory() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting history" , e)
            FirebaseCrashlytics.getInstance().log("Error getting history")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getHistoryByMonth(userId: String, month: Date): List<History> {
        return try {
            val array = db.collection("users")
                .document(userId)
                .collection("history").whereEqualTo("month", month)
            array.get().await()
                .documents.mapNotNull { it.toHistory() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting history" , e)
            FirebaseCrashlytics.getInstance().log("Error getting history")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }
}
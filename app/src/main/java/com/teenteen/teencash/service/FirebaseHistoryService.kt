package com.teenteen.teencash.service

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.teenteen.teencash.data.model.Debtor
import com.teenteen.teencash.data.model.Debtor.Companion.toDebtor
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.data.model.History.Companion.toHistory
import kotlinx.coroutines.tasks.await

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
            db.collection("users")
                .document(userId)
                .collection("history").orderBy("date").get().await()
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
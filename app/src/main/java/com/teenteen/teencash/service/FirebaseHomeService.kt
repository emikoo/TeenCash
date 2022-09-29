package com.teenteen.teencash.service

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.Category.Companion.toCategory
import kotlinx.coroutines.tasks.await

object FirebaseHomeService {
    private const val TAG = "FirebaseHomeService"
    val db = FirebaseFirestore.getInstance()

    suspend fun getCategories(userId: String): List<Category> {
        return try {
            db.collection("users")
                .document(userId)
                .collection("categories").get().await()
                .documents.mapNotNull { it.toCategory() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting category" , e)
            FirebaseCrashlytics.getInstance().log("Error getting category")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }
}
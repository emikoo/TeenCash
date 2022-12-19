package com.teenteen.teencash.service

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.teenteen.teencash.data.model.Debtor
import com.teenteen.teencash.data.model.Debtor.Companion.toDebtor
import kotlinx.coroutines.tasks.await
//TODO: ТУДУ ЕСТЬ КУДА РАСТИ
object FirebaseDebtorService {
    private const val TAG = "FirebaseDebtorService"
    private val db = FirebaseFirestore.getInstance()

    suspend fun getMotherfuckers(userId: String): List<Debtor> {
        return try {
            db.collection("users")
                .document(userId)
                .collection("motherfuckers").get().await()
                .documents.mapNotNull { it.toDebtor() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting motherfuckers" , e)
            FirebaseCrashlytics.getInstance().log("Error getting motherfuckers")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getBloodsuckers(userId: String): List<Debtor> {
        return try {
            db.collection("users")
                .document(userId)
                .collection("bloodsuckers").get().await()
                .documents.mapNotNull { it.toDebtor() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting bloodsuckers" , e)
            FirebaseCrashlytics.getInstance().log("Error getting bloodsuckers")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    fun createMotherfucker(uid: String, docName: String, debtor: Debtor) {
        try {
            db.collection("users")
                .document(uid)
                .collection("motherfuckers").document(docName).set(debtor)
        } catch (e: Exception) {
            Log.e(TAG , "Error creating motherfuckers" , e)
            FirebaseCrashlytics.getInstance().log("Error creating motherfuckers")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun createBloodsucker(uid: String, docName: String, debtor: Debtor) {
        try {
            db.collection("users")
                .document(uid)
                .collection("bloodsuckers").document(docName).set(debtor)
        } catch (e: Exception) {
            Log.e(TAG , "Error creating bloodsuckers" , e)
            FirebaseCrashlytics.getInstance().log("Error creating bloodsuckers")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateMotherfucker(uid: String, docName: String, name: String, amount: Int) {
        try {
            db.collection("users")
                .document(uid)
                .collection("motherfuckers").document(docName).update(
                    mapOf("name" to name,
                "amount" to amount))
        } catch (e: Exception) {
            Log.e(TAG , "Error updating motherfuckers" , e)
            FirebaseCrashlytics.getInstance().log("Error updating motherfuckers")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateBloodsucker(uid: String, docName: String, name: String, amount: Int) {
        try {
            db.collection("users")
                .document(uid)
                .collection("bloodsuckers").document(docName).update(
                    mapOf("name" to name,
                "amount" to amount))
        } catch (e: Exception) {
            Log.e(TAG , "Error updating bloodsuckers" , e)
            FirebaseCrashlytics.getInstance().log("Error updating bloodsuckers")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun deleteMotherfucker(uid: String, name: String) {
        try {
            db.collection("users")
                .document(uid)
                .collection("motherfuckers").document(name).delete()
        } catch (e: Exception) {
            Log.e(TAG , "Error deleting motherfuckers" , e)
            FirebaseCrashlytics.getInstance().log("Error deleting motherfuckers")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun deleteBloodsucker(uid: String, name: String) {
        try {
            db.collection("users")
                .document(uid)
                .collection("bloodsuckers").document(name).delete()
        } catch (e: Exception) {
            Log.e(TAG , "Error deleting bloodsuckers" , e)
            FirebaseCrashlytics.getInstance().log("Error deleting bloodsuckers")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}
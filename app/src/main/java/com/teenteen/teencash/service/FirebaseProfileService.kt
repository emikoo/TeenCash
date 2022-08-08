package com.teenteen.teencash.service

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.Category.Companion.toCategory
import com.teenteen.teencash.data.model.User
import com.teenteen.teencash.data.model.User.Companion.toUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirebaseProfileService {
    private const val TAG = "FirebaseProfileService"
    suspend fun getProfileData(userId: String): User? {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("users")
                .document(userId).get().await().toUser()
        } catch (e: Exception) {
            Log.e(TAG , "Error getting user details" , e)
            FirebaseCrashlytics.getInstance().log("Error getting user details")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getCategories(userId: String): List<Category> {
        val db = FirebaseFirestore.getInstance()
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

    suspend fun getPiggyBanks(userId: String): List<Category> {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("users")
                .document(userId)
                .collection("piggy_banks").get().await()
                .documents.mapNotNull { it.toCategory() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting piggy bank" , e)
            FirebaseCrashlytics.getInstance().log("Error getting category")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun deleteCategory(userId: String, docName: String) {
        val db = FirebaseFirestore.getInstance()
        val category_ref = db.collection("users")
            .document(userId)
            .collection("categories").document(docName)
        try {
            category_ref.delete()
        } catch (e: Exception) {
            Log.e(TAG , "Error deleting category" , e)
            FirebaseCrashlytics.getInstance().log("Error deleting category")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    suspend fun deletePiggy(userId: String, docName: String) {
        val db = FirebaseFirestore.getInstance()
        val piggy_ref = db.collection("users")
            .document(userId)
            .collection("piggy_banks").document(docName)
        try {
            piggy_ref.delete()
        } catch (e: Exception) {
            Log.e(TAG , "Error deleting piggy bank" , e)
            FirebaseCrashlytics.getInstance().log("Error deleting piggy bank")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    @ExperimentalCoroutinesApi
    fun getPosts(userId: String): Flow<List<Category>> {
        val db = FirebaseFirestore.getInstance()
        return callbackFlow {
            val listenerRegistration = db.collection("users")
                .document(userId)
                .collection("categories")
                .addSnapshotListener { querySnapshot: QuerySnapshot? , firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching categories" ,
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot !!.documents.mapNotNull { it.toCategory() }
                    trySend(map).isSuccess
                }
            awaitClose {
                Log.d(TAG , "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }
    }
}
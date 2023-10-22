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

    suspend fun getEarnings(userId: String): List<Category> {
        return try {
            db.collection("users")
                .document(userId)
                .collection("earnings").get().await()
                .documents.mapNotNull { it.toCategory() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting earnings" , e)
            FirebaseCrashlytics.getInstance().log("Error getting earnings")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getPiggyBanks(userId: String): List<Category> {
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

    suspend fun getAchievements(userId: String): List<Category> {
        return try {
            db.collection("users")
                .document(userId)
                .collection("achievements").get().await()
                .documents.mapNotNull { it.toCategory() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting achievements" , e)
            FirebaseCrashlytics.getInstance().log("Error getting achievements")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getBalance(uid: String): Int? {
        return try {
            db.collection("users")
                .document(uid)
                .collection("statistics").document("info")
                .get().await().get("balance").toString().toInt()
        } catch (e: Exception) {
            Log.e(TAG , "Error getting balance" , e)
            FirebaseCrashlytics.getInstance().log("Error getting balance")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getCurrency(uid: String): String? {
        return try {
            db.collection("users")
                .document(uid)
                .collection("statistics").document("info")
                .get().await().get("currency").toString()
        } catch (e: Exception) {
            Log.e(TAG , "Error getting balance" , e)
            FirebaseCrashlytics.getInstance().log("Error getting balance")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getLimit(uid: String): Int? {
        return try {
            db.collection("users")
                .document(uid)
                .collection("statistics").document("info")
                .get().await().get("limitPerDay").toString().toInt()
        } catch (e: Exception) {
            Log.e(TAG , "Error getting limitPerDay" , e)
            FirebaseCrashlytics.getInstance().log("Error getting limitPerDay")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getSpentAmount(uid: String): Int? {
        return try {
            db.collection("users")
                .document(uid)
                .collection("statistics").document("info")
                .get().await().get("spentAmount").toString().toInt()
        } catch (e: Exception) {
            Log.e(TAG , "Error getting spentAmount" , e)
            FirebaseCrashlytics.getInstance().log("Error getting spentAmount")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getSavedAmount(uid: String): Int? {
        return try {
            db.collection("users")
                .document(uid)
                .collection("statistics").document("info")
                .get().await().get("savedAmount").toString().toInt()
        } catch (e: Exception) {
            Log.e(TAG , "Error getting saved" , e)
            FirebaseCrashlytics.getInstance().log("Error getting saved")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    fun updateBalance(userId: String , balance: Int) {
        try {
            db.collection("users")
                .document(userId)
                .collection("statistics").document("info").update("balance" , balance)
        } catch (e: Exception) {
            Log.e(TAG , "Error updating balance" , e)
            FirebaseCrashlytics.getInstance().log("Error updating balance")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateSavedAmount(userId: String , amount: Int) {
        try {
            db.collection("users")
                .document(userId)
                .collection("statistics").document("info").update("savedAmount" , amount)
        } catch (e: Exception) {
            Log.e(TAG , "Error updating saved" , e)
            FirebaseCrashlytics.getInstance().log("Error updating saved")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateLimit(userId: String , amount: Int) {
        try {
            db.collection("users")
                .document(userId)
                .collection("statistics").document("info").update("limitPerDay" , amount)
        } catch (e: Exception) {
            Log.e(TAG , "Error updating limitPerDay" , e)
            FirebaseCrashlytics.getInstance().log("Error updating limitPerDay")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateSpentAmount(userId: String , amount: Int) {
        try {
            db.collection("users")
                .document(userId)
                .collection("statistics").document("info").update("spentAmount" , amount)
        } catch (e: Exception) {
            Log.e(TAG , "Error updating spentAmount" , e)
            FirebaseCrashlytics.getInstance().log("Error updating spentAmount")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateCategory(userId: String , docName: String) {
        try {
            db.collection("users")
                .document(userId)
                .collection("categories").document(docName).update("firstAmount" , 0)
        } catch (e: Exception) {
            Log.e(TAG , "Error spending category" , e)
            FirebaseCrashlytics.getInstance().log("Error spending category")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun spendCategory(userId: String , docName: String , firstAmount: Int) {
        try {
            db.collection("users")
                .document(userId)
                .collection("categories").document(docName).update("firstAmount" , firstAmount)
        } catch (e: Exception) {
            Log.e(TAG , "Error spending category" , e)
            FirebaseCrashlytics.getInstance().log("Error spending category")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun saveMoneyPiggy(userId: String , docName: String , firstAmount: Int) {
        try {
            db.collection("users")
                .document(userId)
                .collection("piggy_banks").document(docName).update("firstAmount" , firstAmount)
        } catch (e: Exception) {
            Log.e(TAG , "Error saving piggy" , e)
            FirebaseCrashlytics.getInstance().log("Error saving piggy")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateCategory(uid: String, docName: String, name: String, firstAmount: Int, secondAmount: Int) {
        try {
            db.collection("users").document(uid).collection("categories")
                .document(docName).update(mapOf("name" to name,
                "firstAmount" to firstAmount, "secondAmount" to secondAmount))
        } catch (e: Exception) {
            Log.e(TAG , "Error editing category" , e)
            FirebaseCrashlytics.getInstance().log("Error editing category")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updatePiggy(uid: String, docName: String , name: String , firstAmount: Int , secondAmount: Int) {
        try {
            db.collection("users").document(uid).collection("piggy_banks")
                .document(docName).update(mapOf("name" to name,
                    "firstAmount" to firstAmount, "secondAmount" to secondAmount))
        } catch (e: Exception) {
            Log.e(TAG , "Error editing piggy" , e)
            FirebaseCrashlytics.getInstance().log("Error editing piggy")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun deleteCategory(userId: String, docName: String) {
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

    fun deletePiggy(userId: String , docName: String) {
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

    fun deleteEarning(userId: String , docName: String) {
        val earning_ref = db.collection("users")
            .document(userId)
            .collection("earnings").document(docName)
        try {
            earning_ref.delete()
        } catch (e: Exception) {
            Log.e(TAG , "Error deleting earning" , e)
            FirebaseCrashlytics.getInstance().log("Error deleting earning")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun deleteAchievement(userId: String , docName: String) {
        val piggy_ref = db.collection("users")
            .document(userId)
            .collection("achievements").document(docName)
        try {
            piggy_ref.delete()
        } catch (e: Exception) {
            Log.e(TAG , "Error deleting achievement" , e)
            FirebaseCrashlytics.getInstance().log("Error deleting achievement")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun createAchievement(uid: String, docName: String, item: Category) {
        try {
            db.collection("users").document(uid)
                .collection("achievements").document(docName).set(item)
        } catch (e: Exception) {
            Log.e(TAG , "Error creating achievement" , e)
            FirebaseCrashlytics.getInstance().log("Error creating achievement")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateCurrency(uid: String, currency: String) {
        try {
            db.collection("users").document(uid)
                .collection("statistics").document("info").update("currency", currency)
        } catch (e: Exception) {
            Log.e(TAG , "Error updating currency" , e)
            FirebaseCrashlytics.getInstance().log("Error updating currency")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun setImage(uid: String, docName: String, image: String) {
        try {
            db.collection("users").document(uid).collection("achievements")
                .document(docName).update("image", image)
        } catch (e: Exception) {
            Log.e(TAG , "Error setting image" , e)
            FirebaseCrashlytics.getInstance().log("Error setting image")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}
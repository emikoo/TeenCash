package com.teenteen.teencash.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot

data class Category(
    var iconId: Int ,
    var secondAmount: Int ,
    var name: String ,
    var firstAmount: Int? = null
) {
    companion object {
        fun DocumentSnapshot.toCategory(): Category? {
            try {
                val icon = get("iconId").toString().toInt()
                val limit = get("secondAmount").toString().toInt()
                val name = getString("name") !!
                val spentToday = get("firstAmount")?.toString()?.toInt()
                return Category(icon , limit , name , spentToday)
            } catch (e: Exception) {
                Log.e(TAG , "Error converting category" , e)
                FirebaseCrashlytics.getInstance().log("Error converting category")
                FirebaseCrashlytics.getInstance().setCustomKey("categoryId" , id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }

        private const val TAG = "Category"
    }
}

data class CategoryName(
    val id: Int,
    val name: String ,
    var is_used: Boolean = false
)
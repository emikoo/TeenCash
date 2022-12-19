package com.teenteen.teencash.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
//TODO: ТУДУ ЕСТЬ КУДА РАСТИ (ХОТЯБЫ ИЗБАВИТЬСЯ ОТ !!)
data class Category(
    var iconId: Int ,
    var secondAmount: Int ,
    var name: String ,
    var docName: String,
    var firstAmount: Int,
    var image: String = "",
    var currency: String? = "KGS"
) {
    companion object {
        fun DocumentSnapshot.toCategory(): Category? {
            try {
                val icon = get("iconId").toString().toInt()
                val secondAmount = get("secondAmount").toString().toInt()
                val name = getString("name") !!
                val docName = getString("docName") !!
                val firstAmount = get("firstAmount").toString().toInt()
                val image = getString("image") !!
                val currency = getString("currency") !!
                return Category(icon , secondAmount , name , docName, firstAmount, image, currency)
            } catch (e: Exception) {
                //ЭТО ВЕЗДЕ ПОВТОРЯЕТСЯ, МОЖНО ЖЕ СДЕЛАТЬ КРУТО!!
                Log.e(TAG , "Error converting category" , e)
                FirebaseCrashlytics.getInstance().log("Error converting category")
                FirebaseCrashlytics.getInstance().setCustomKey("categoryId"     , id)
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
package com.teenteen.teencash.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
//TODO: ТУДУ ЕСТЬ КУДА РАСТИ (ХОТЯБЫ ИЗБАВИТЬСЯ ОТ !!)
data class History(
    var name: String,
    var amount: Int,
    var spent: Boolean,
    var date: Date,
    var time: Date,
    var month: Date,
    var image: Int,
    var currency: String
) {
    companion object {
        fun DocumentSnapshot.toHistory(): History? {
            try {
                val name = getString("name") !!
                val amount = get("amount").toString().toInt()
                val spent = getBoolean("spent") !!
                val date = getDate("date") !!
                val time = getDate("time") !!
                val month = getDate("month") !!
                val image = get("image").toString().toInt()
                val currency = getString("currency") !!
                return History(name , amount, spent, date, time, month, image, currency)
            } catch (e: Exception) {
                //ЭТО ВЕЗДЕ ПОВТОРЯЕТСЯ, МОЖНО ЖЕ СДЕЛАТЬ КРУТО!!
                Log.e(TAG , "Error converting history" , e)
                FirebaseCrashlytics.getInstance().log("Error converting history")
                FirebaseCrashlytics.getInstance().setCustomKey("historyId" , id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }

        private const val TAG = "History"
    }
}

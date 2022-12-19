package com.teenteen.teencash.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
//TODO: ТУДУ ЕСТЬ КУДА РАСТИ (ХОТЯБЫ ИЗБАВИТЬСЯ ОТ !!)
data class Debtor(
    val docName: String,
    val name: String,
    val amount: Int,
    var currency: String? = "KGS"
) {
    companion object {
        fun DocumentSnapshot.toDebtor(): Debtor? {
            return try {
                val docName = getString("docName") !!
                val name = getString("name") !!
                val amount = get("amount").toString().toInt()
                val currency = getString("currency") !!
                Debtor(docName, name , amount, currency)
            } catch (e: Exception) {
                //ЭТО ВЕЗДЕ ПОВТОРЯЕТСЯ, МОЖНО ЖЕ СДЕЛАТЬ КРУТО!!
                Log.e(TAG , "Error converting debtor" , e)
                FirebaseCrashlytics.getInstance().log("Error converting debtor")
                FirebaseCrashlytics.getInstance().setCustomKey("debtorId" , id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private const val TAG = "Debtor"
    }
}

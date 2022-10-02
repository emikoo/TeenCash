package com.teenteen.teencash.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot

data class Debtor(
    val docName: String,
    val name: String,
    val amount: Int,
    var currency: String? = "KGS"
) {
    companion object {
        fun DocumentSnapshot.toDebtor(): Debtor? {
            try {
                val docName = getString("docName") !!
                val name = getString("name") !!
                val amount = get("amount").toString().toInt()
                val currency = getString("currency") !!
                return Debtor(docName, name , amount, currency)
            } catch (e: Exception) {
                Log.e(TAG , "Error converting debtor" , e)
                FirebaseCrashlytics.getInstance().log("Error converting debtor")
                FirebaseCrashlytics.getInstance().setCustomKey("debtorId" , id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }

        private const val TAG = "Debtor"
    }
}

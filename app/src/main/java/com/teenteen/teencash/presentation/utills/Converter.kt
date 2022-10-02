package com.teenteen.teencash.presentation.utills

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

fun Int.convertAmount(settingsCurrency: String, currentCurrency: String): Int {
    Log.d("gfhgjgjh", settingsCurrency)
    return if (settingsCurrency != currentCurrency) {
        val link = "from${currentCurrency}to$settingsCurrency"
        var convertedAmount = this.toDouble()
        val gap = link.getExchangeRates()
        convertedAmount *= gap
        convertedAmount.toInt()
    } else this
}

fun String.getExchangeRates(): Double {
    return when(this) {
        "fromEURtoKGS" -> 78.597881
        "fromEURtoUSD" -> 0.98030335
        "fromUSDtoEUR" -> 1.0200924
        "fromUSDtoKGS" -> 80.177102
        "fromKGStoUSD" -> 0.012472389
        "fromKGStoEUR" -> 0.012722989
        else -> 0.0
    }
}

fun getField(field: String): Double {
    var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var amount = 0.00
    firestore.collection("currency")
        .get()
        .addOnSuccessListener {
            amount = it.documents[0].data?.get(field).toString().toDouble()
        }
        .addOnFailureListener {
            it.printStackTrace()
        }
    return amount
}

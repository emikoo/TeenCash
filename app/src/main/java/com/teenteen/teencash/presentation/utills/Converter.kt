package com.teenteen.teencash.presentation.utills

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

fun Int.convertAmount(settingsCurrency: String, currentCurrency: String): Int {
    Log.d("gfhgjgjh", settingsCurrency)
    return if (settingsCurrency != currentCurrency) {
        val link = "from${currentCurrency}to$settingsCurrency"
        var convertedAmount = this.toDouble()
        val gap = link.getExchangeRates()
        convertedAmount *= gap
        convertedAmount.roundToInt()
    } else this
}
//TODO: алвыашвыщаылвоалывалыдвоалдывоалдывоалдоывалд
fun Int.convertSettingsAmount(settingsCurrency: String, currentCurrency: String): Int {
    Log.d("gfhgjgjh", settingsCurrency)
    return if (settingsCurrency != currentCurrency) {
        val link = "from${settingsCurrency}to$currentCurrency"
        var convertedAmount = this.toDouble()
        val gap = link.getExchangeRates()
        convertedAmount *= gap
        convertedAmount.roundToInt()
    } else this
}
//Я ССЫЛКУ В ЧАТИК СКИДЫВАЛ НА АКТУЛЬНЫЕ КУРСЫ, МОЖЕТЕ ТУ АПИШКУ ЗАЮЗАТЬ)
fun String.getExchangeRates(): Double {
    return when(this) {
        "fromEURtoKGS" -> 82.96
        "fromEURtoUSD" -> 1.00
        "fromUSDtoEUR" -> 1.00
        "fromUSDtoKGS" -> 83.25
        "fromKGStoUSD" -> 0.012
        "fromKGStoEUR" -> 0.012
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

package com.teenteen.teencash.service

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.InfoStatistic

object FirebaseAuthService {
    private const val TAG = "FirebaseAuthService"
    private val db = FirebaseFirestore.getInstance()

    fun createDefaultGoal(uid: String, name: String) {
        val defaultGoal = Category(
            name = name,
            secondAmount = 5000 ,
            iconId = 777 ,
            firstAmount = 0,
            docName = "${name}5000",
            image = ""
        )
        try {
            db.collection("users").document(uid).collection("piggy_banks")
                .document("${name}5000").set(defaultGoal)
        } catch (e: Exception) {
            Log.e(TAG , "Error creating default goal" , e)
            FirebaseCrashlytics.getInstance().log("Error creating default goal")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun createDefaultCategory(uid: String, name: String) {
        val defaultCategory = Category(
            name = name ,
            secondAmount = 50 ,
            iconId = 0 ,
            firstAmount = 0,
            docName = "${name}50",
            image = ""
        )
        try {
            db.collection("users").document(uid).collection("categories")
                .document("${name}5000").set(defaultCategory)
        } catch (e: Exception) {
            Log.e(TAG , "Error creating default category" , e)
            FirebaseCrashlytics.getInstance().log("Error creating default category")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun createInfoDoc(uid: String) {
        try {
            db.collection("users").document(uid).collection("statistics")
                .document("info").set(InfoStatistic(0 , 0 , 0 , 0))
        } catch (e: Exception) {
            Log.e(TAG , "Error creating default info" , e)
            FirebaseCrashlytics.getInstance().log("Error creating default info")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , uid)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}
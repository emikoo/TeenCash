package com.teenteen.teencash.data.local

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class PrefsSettings(private val context: Context) {
    private lateinit var prefs: SharedPreferences
    private lateinit var prefsEditor: SharedPreferences.Editor
    private val LANGUAGE = "LANGUAGE"
    private val UID = "UID"

    init {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefsEditor = prefs.edit()
    }

    fun setFirstTimeLaunch(isFirstTime: Int) {
        prefsEditor.putInt(IS_FIRST_TIME_LAUNCH, isFirstTime).commit()
    }

    fun isFirstTimeLaunch(): Int = prefs.getInt(IS_FIRST_TIME_LAUNCH, FIRST_TIME)

    fun saveSettingsLanguage(language: String?) {
        prefsEditor.putString(LANGUAGE, language).apply()
    }

    fun getSettingsLanguage(): String {
        return prefs.getString(LANGUAGE, Locale.getDefault().language) ?: Locale.getDefault().language
    }

    fun saveCurrentUserId(uid: String?) {
        prefsEditor.putString(UID, uid).apply()
    }

    fun getCurrentUserId(): String {
        return prefs.getString(UID, "") ?: ""
    }

    companion object {
        private const val PREFS_NAME = "TeenCash"
        private const val PRIVATE_MODE = 0
        const val FIRST_TIME = 1
        const val NOT_AUTH = 2
        const val USER = 3
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        lateinit var instance: PrefsSettings
    }
}
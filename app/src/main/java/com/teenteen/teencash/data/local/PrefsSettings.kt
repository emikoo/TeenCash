package com.teenteen.teencash.data.local

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class PrefsSettings(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME , Context.MODE_PRIVATE)
    private var prefsEditor: SharedPreferences.Editor = prefs.edit()
    private val LANGUAGE = "LANGUAGE"
    private val DARK_THEME_MODE = "DARK_THEME_MODE"
    private val CURRENT_DAY = "CURRENT_DAY"
    private val UID = "UID"
    private val SETTINGS_CURRENCY = "SETTINGS_CURRENCY"

    fun setFirstTimeLaunch(isFirstTime: Int) {
        prefsEditor.putInt(IS_FIRST_TIME_LAUNCH , isFirstTime).commit()
    }

    fun isFirstTimeLaunch(): Int = prefs.getInt(IS_FIRST_TIME_LAUNCH , FIRST_TIME)

    fun saveSettingsLanguage(language: String?) {
        prefsEditor.putString(LANGUAGE , language).apply()
    }

    fun getSettingsLanguage(): String {
        return prefs.getString(LANGUAGE , Locale.getDefault().language)
            ?: Locale.getDefault().language
    }

    fun saveDarkThemeMode(darkMode: Boolean) {
        prefsEditor.putBoolean(DARK_THEME_MODE , darkMode).apply()
    }

    fun getDarkThemeMode(): Boolean {
        return prefs.getBoolean(DARK_THEME_MODE , false)
    }

    fun saveCurrentUserId(uid: String?) {
        prefsEditor.putString(UID , uid).apply()
    }

    fun getCurrentUserId(): String {
        return prefs.getString(UID , "") ?: ""
    }

    fun saveSettingsCurrency(currency: String?) {
        prefsEditor.putString(SETTINGS_CURRENCY , currency).apply()
    }

    fun getSettingsCurrency(): String {
        return prefs.getString(SETTINGS_CURRENCY , "") ?: "EUR"
    }

    fun saveCurrentDay(dateInString: String) {
        prefsEditor.putString(CURRENT_DAY , dateInString).apply()
    }

    fun getCurrentDay(): String {
        return prefs.getString(CURRENT_DAY , "") ?: ""
    }

    companion object {
        private const val PREFS_NAME = "TeenCash"
        const val FIRST_TIME = 1
        const val NOT_AUTH = 2
        const val USER = 3
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    }
}
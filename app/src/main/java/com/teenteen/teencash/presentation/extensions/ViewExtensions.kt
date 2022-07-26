package com.teenteen.teencash.presentation.extensions

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teenteen.teencash.R
import com.teenteen.teencash.data.local.PrefsSettings
import java.util.*

fun View.isGone() {
    this.visibility = View.GONE
}

fun View.isInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible() {
    this.visibility = View.VISIBLE
}

fun BottomSheetDialogFragment.show(fragmentManager: FragmentManager?) {
    val bottomSheetDialogFragment: BottomSheetDialogFragment = this
    fragmentManager?.let {
        bottomSheetDialogFragment.show(
            it ,
            bottomSheetDialogFragment.tag
        )
    }
}

fun PreferenceManager.updateLanguage(lang: String, context: Context, prefsSettings: PrefsSettings) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val config = Configuration()
    config.locale = locale
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
    prefsSettings.saveSettingsLanguage(lang)
}

fun String.toSymbol(): String {
    var textWithoutCaps = ""
    when(this) {
        "KGS" -> textWithoutCaps = "kgs"
        "KZT" -> textWithoutCaps = "kzt"
        "UZS" -> textWithoutCaps = "uzs"
        "EUR" -> textWithoutCaps = "€"
        "USD" -> textWithoutCaps = "$"
    }
    return  textWithoutCaps
}
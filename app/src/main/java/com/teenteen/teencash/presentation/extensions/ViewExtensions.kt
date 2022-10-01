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
import java.text.SimpleDateFormat
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

fun Date.dateToString(locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun PreferenceManager.updateLanguage(lang: String, context: Context, prefsSettings: PrefsSettings) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val config = Configuration()
    config.locale = locale
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
    prefsSettings.saveSettingsLanguage(lang)
}

fun Context.showNoConnectionToast() {
    Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
}
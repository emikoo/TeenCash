package com.teenteen.teencash.presentation.extensions

import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
    val formatter = SimpleDateFormat("dd/MM/yyyy", locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

}
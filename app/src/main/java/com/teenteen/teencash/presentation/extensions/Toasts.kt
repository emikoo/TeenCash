package com.teenteen.teencash.presentation.extensions

import android.content.Context
import android.widget.Toast
import com.teenteen.teencash.R

fun Context.showNoConnectionToast() {
    Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
}

fun Context.showItemExistsToast() {
    Toast.makeText(this , getString(R.string.item_exists) , Toast.LENGTH_LONG).show()
}

fun Context.showToast(text: String) {
    Toast.makeText(this , text , Toast.LENGTH_SHORT).show()
}
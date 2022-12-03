package com.teenteen.teencash.presentation.utills

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.teenteen.teencash.R

fun showAlertDialog(
    context: Context ,
    fragment: Fragment ,
    titleText: String? = null ,
    subtitleText: String? = null ,
    buttonText: String? = null ,
    action: () -> Unit
) {
    val alert = AlertDialog.Builder(context)
    val view: View = fragment.layoutInflater.inflate(R.layout.layout_alert , null)
    alert.setView(view)
    val positiveButton: Button = view.findViewById(R.id.button_positive)
    val negativeButton: Button = view.findViewById(R.id.button_negative)
    val title: TextView = view.findViewById(R.id.title)
    val subtitle: TextView = view.findViewById(R.id.subtitle)

    val dialog = alert.create()
    dialog.setCancelable(false)
    title.text = titleText
    subtitle.text = subtitleText
    positiveButton.text = buttonText
    negativeButton.text = context.resources.getString(R.string.cancel)
    positiveButton.setOnClickListener {
        action()
        dialog.dismiss()
    }
    negativeButton.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}

fun showAlertDialog(
    context: Context ,
    fragment: Fragment ,
    titleText: String? = null ,
    subtitleText: String? = null ,
    buttonText: String? = null
) {
    val alert = AlertDialog.Builder(context)
    val view: View = fragment.layoutInflater.inflate(R.layout.layout_alert , null)
    alert.setView(view)
    val positiveButton: Button = view.findViewById(R.id.button_positive)
    val title: TextView = view.findViewById(R.id.title)
    val subtitle: TextView = view.findViewById(R.id.subtitle)

    val dialog = alert.create()
    dialog.setCancelable(false)
    title.text = titleText
    subtitle.text = subtitleText
    positiveButton.text = buttonText
    positiveButton.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}
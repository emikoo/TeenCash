package com.teenteen.teencash.presentation.extensions

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.teenteen.teencash.R

fun showAlertDialog(
    context: Context,
    fragment: Fragment,
) {
    val alert = AlertDialog.Builder(context)
    val view: View = fragment.layoutInflater.inflate(R.layout.layout_alert, null)
    alert.setView(view)
    val positiveButton: Button = view.findViewById(R.id.button_positive)
    val title: TextView = view.findViewById(R.id.title)
    val subtitle: TextView = view.findViewById(R.id.subtitle)

    val dialog = alert.create()
    dialog.setCancelable(false)
    title.text = context.resources.getString(R.string.verify_your_email)
    subtitle.text = context.resources.getString(R.string.verify_your_email_subtitle)
    positiveButton.text = context.resources.getString(R.string.ok)
    positiveButton.setOnClickListener{
        dialog.dismiss()
    }
    dialog.show()
}
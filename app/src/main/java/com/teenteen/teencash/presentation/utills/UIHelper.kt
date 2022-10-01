package com.teenteen.teencash.presentation.utills

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.widget.Toast
import com.teenteen.teencash.R

fun vibrate(context: Context?) {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500 , VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
    }
}

class ProgressDialog {
    companion object {
        fun progressDialog(context: Context): Dialog {
            val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            val inflate =
                LayoutInflater.from(context).inflate(R.layout.layout_progress_dialog , null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window !!.setBackgroundDrawable(
                context.getDrawable(R.drawable.bg_progress_dialog)
            )
            return dialog
        }
    }
}

fun checkInternetConnection(connectedAction: () -> Unit, context: Context, noInternetAction: () -> Unit) {
    if (internetIsConnected(context)) {
        connectedAction()
    } else {
        noInternetAction()
    }
}

fun internetIsConnected(context: Context): Boolean {
    var connected = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connected =
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
    return if (!connected) {
        Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        false
    } else {
        true
    }
}
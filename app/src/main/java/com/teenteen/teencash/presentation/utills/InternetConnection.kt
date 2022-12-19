package com.teenteen.teencash.presentation.utills

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.teenteen.teencash.R

fun checkInternetConnection(connectedAction: () -> Unit , context: Context , noInternetAction: () -> Unit) {
    if (internetIsConnected(context)) {
        connectedAction()
    } else {
        noInternetAction()
    }
}

fun checkInternetConnection(connectedAction: () -> Unit, context: Context) {
    if (internetIsConnected(context)) {
        connectedAction()
    } else {
        Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
    }
}
//TODO: ТУТ УЖЕ ПОЛОВИНА ДЕПРЕКЕЙТЕД, СТОИТ ПОИСКАТЬ РЕШЕНИЕ ПОЛУЧШЕ
fun internetIsConnected(context: Context): Boolean {
    var connected = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connected =
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
    return connected
}
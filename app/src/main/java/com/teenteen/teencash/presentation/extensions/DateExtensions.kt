package com.teenteen.teencash.presentation.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.dateToString(locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", locale)
    return formatter.format(this)
}

fun Date.dateTimeToString(locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun getCurrentDate(): Date {
    val formatter = SimpleDateFormat("dd.MM.yyyy")
    val today = Date()
    return formatter.parse(formatter.format(today))
}

fun getCurrentWeek(): Date {
    val formatter = SimpleDateFormat("dd.MM.yyyy")
    val today = formatter.parse(formatter.format(Date()))
    var newDate = Date(today.time - 604800000L)
    val calendar = Calendar.getInstance()
    calendar.time = today
    calendar.add(Calendar.DAY_OF_YEAR , - 7)
    newDate = calendar.time
    return formatter.parse(formatter.format(newDate))
}

fun getCurrentMonth(): Date {
    val formatter = SimpleDateFormat("MM")
    val today = Date()
    return formatter.parse(formatter.format(today))
}
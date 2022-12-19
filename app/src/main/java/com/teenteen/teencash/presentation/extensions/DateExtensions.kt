package com.teenteen.teencash.presentation.extensions

import java.text.SimpleDateFormat
import java.util.*

private const val DATE_MONTH = "MM"
private const val DATE_STANDART = "dd.MM.yyyy"
private const val DATE_AND_TIME_STANDART = "dd MMM yyyy HH:mm"

fun Date.dateToString(locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(DATE_STANDART, locale)
    return formatter.format(this)
}

fun Date.dateTimeToString(locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(DATE_AND_TIME_STANDART, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun getCurrentDate(): Date {
    val formatter = SimpleDateFormat(DATE_STANDART)
    val today = Date()
    return formatter.parse(formatter.format(today)) //TODO: Здесь точно вернется текущий день?
}

fun getCurrentWeek(): Date {
    val formatter = SimpleDateFormat(DATE_STANDART)
    val today = formatter.parse(formatter.format(Date()))
    var newDate = Date(today.time - 604800000L) //TODO: Зачем?
    val calendar = Calendar.getInstance()
    calendar.time = today
    calendar.add(Calendar.DAY_OF_YEAR, -7)
    newDate = calendar.time
    return formatter.parse(formatter.format(newDate)) //TODO: Здесь точно вернется текущий неделя?
}

fun getCurrentMonth(): Date {
    val formatter = SimpleDateFormat(DATE_MONTH)
    val today = Date()
    return formatter.parse(formatter.format(today)) //TODO: Здесь точно вернется текущий месяц?
}
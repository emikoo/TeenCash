package com.teenteen.teencash.data.model

data class InfoStatistic(
    val balance: Int? = 0,
    val currency: String? = "EUR",
    val savedAmount: Int? = 0,
    val limitPerDay: Int? = 0,
    val spentAmount: Int? = 0
)

package com.teenteen.teencash.data.model

data class InfoStatistic(
    val balance: Int? = 0,
    val balanceCurrency: String? = "KGS",
    val savedAmount: Int? = 0,
    val limitPerDay: Int? = 0,
    val limitCurrency: String? = "KGS",
    val spentAmount: Int? = 0
)

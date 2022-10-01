package com.teenteen.teencash.data.model

import java.util.*

data class History(
    var name: String,
    var amount: Int,
    var isSpent: Boolean,
    var date: Date,
    var image: Int
)

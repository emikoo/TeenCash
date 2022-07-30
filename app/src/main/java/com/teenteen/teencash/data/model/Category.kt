package com.teenteen.teencash.data.model

data class Category(
    val id: Int,
    var name: String,
    var icon: Int,
    var limit: Int,
    var spentToday: Int? = null
)

data class CategoryName(
    val name: String,
    var is_used: Boolean = false
)



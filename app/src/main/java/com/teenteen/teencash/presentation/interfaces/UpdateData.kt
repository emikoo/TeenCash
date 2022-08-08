package com.teenteen.teencash.presentation.interfaces

import com.teenteen.teencash.data.model.Category

interface UpdateData {
    fun updateCategory(newCategory: Category)
    fun updatePiggyBank(newGoal: Category)
}
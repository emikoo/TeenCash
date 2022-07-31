package com.teenteen.teencash.presentation.interfaces

import com.teenteen.teencash.data.model.CategoryName

interface PickerItem {
    fun chosenIcon(item: CategoryName)
}
package com.teenteen.teencash.presentation.utills

enum class CategoryAdapterKeys{
    CATEGORY, EARNINGS, PIGGY_BANK
}

enum class DebtorAdapterKeys{
    MOTHERFUCKER, BLOODSUCKER
}

enum class AddBottomSheetKeys(var colName: String? = null, var docName: String? = null, var imageID: Int? = null){
    CREATE_PIGGY("piggy_banks", imageID = 777), SPENT_CARD, ADD_MONEY_TO_PIGGY("statistics", "info", 777), SET_LIMIT,
    ADD_BALANCE("statistics", "info", 1313),
    CREATE_MOTHERFUCKER("motherfuckers", imageID = 666),
    CREATE_BLOODSUCKER("bloodsuckers", imageID = 666), UPDATE_MOTHERFUCKER(imageID = 666),
    UPDATE_BLOODSUCKER(imageID = 666), UPDATE_SPENDING_CARD, UPDATE_PIGGY, UPDATE_BALANCE(imageID = 1313),
    ADD_EARNING
}

enum class ListBottomSheetKeys{
    CATEGORY_SETTINGS, PIGGY_BANK_SETTINGS, CHANGE_LANGUAGE, CHANGE_CURRENCY
}
package com.teenteen.teencash.presentation.interfaces

interface UpdateData {
    fun updateSpendingCard()
    fun updateEarnings()
    fun updatePiggyBank()
    fun updateStatistics()
    fun updateMFList()
    fun updateBSList()
    fun achieved()
}

//TODO: ТУТ И АПДЕЙТ ЛЕНГВИЧ-СЕНДВИЧ СПРЯТАЛСЯ)
interface UpdateLanguage {
    fun updateLanguage()
}
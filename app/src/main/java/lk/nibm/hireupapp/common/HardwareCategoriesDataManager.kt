package lk.nibm.hireupapp.common

import lk.nibm.hireupapp.model.HardwareCategoriesData

object HardwareCategoriesDataManager {
    private var HardwareCategory: HardwareCategoriesData? = null

    fun setHardwareCategory(HardwareCategoriesData: HardwareCategoriesData){
        HardwareCategory = HardwareCategoriesData
    }
    fun getHardwareCategory(): HardwareCategoriesData?{
        return HardwareCategory
    }
}
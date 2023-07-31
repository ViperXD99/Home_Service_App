package lk.nibm.hireupapp.common

import lk.nibm.hireupapp.model.HardwareProductsData

object HardwareProductsDataManager {
    private var hardwareProduct: HardwareProductsData? = null

    fun setHardwareProduct(hardwareProductsData: HardwareProductsData) {
        hardwareProduct = hardwareProductsData
    }

    fun getHardwareProduct(): HardwareProductsData? {
        return hardwareProduct
    }
}

package lk.nibm.hireupapp.common

import lk.nibm.hireupapp.model.Hardware

object ShopDataManager {
    private var shop: Hardware? = null
    fun setShop(shopData: Hardware){
        shop = shopData
    }
    fun getShop(): Hardware?{
        return shop
    }
}
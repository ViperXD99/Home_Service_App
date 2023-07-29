package lk.nibm.hireupapp.common

import lk.nibm.hireupapp.model.AddressDataClass

object AddressDataManager {
    private var address: AddressDataClass? = null
    fun setAddress(addressData: AddressDataClass){
        address = addressData
    }
    fun getAddress(): AddressDataClass?{
        return address
    }
}
package lk.nibm.hireupapp.common

import lk.nibm.hireupapp.model.ServiceProviders

object ServiceProviderDataManager {
    private var serviceProvider: ServiceProviders? = null
    fun setProvider(serviceProviderData: ServiceProviders){
        serviceProvider = serviceProviderData
    }
    fun getProvider(): ServiceProviders?{
        return serviceProvider
    }
}
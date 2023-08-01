package lk.nibm.hireupapp.model

data class ShopOrders(
    val orderID: String? = null,
    val customerID: String? = null,
    val hardwareID : String? = null,
    val finalTotal : Double? = null,
    val orderStatus : String? = null,
    val paymentOption : String? = null,
    val products : List<ShopOrderItems>
)

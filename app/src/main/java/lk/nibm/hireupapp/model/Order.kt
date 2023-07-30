package lk.nibm.hireupapp.model

data class Order(
    val addressID : String? = null,
    val arrivalConfirm : String? = null,
    val bookingDate : String? = null,
    val completeConfirm : String? = null,
    val customerID : String? = null,
    val description : String? = null,
    val paid : String? = null,
    val orderID : String? = null,
    val providerID : String? = null,
    val serviceID : String? = null,
    val spArrivalConfirm : String? = null,
    val spCompleteConfirm : String? = null,
    val status : String? = null
)

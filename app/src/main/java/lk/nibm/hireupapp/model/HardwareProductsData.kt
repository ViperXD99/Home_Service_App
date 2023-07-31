package lk.nibm.hireupapp.model

data class HardwareProductsData(
    val id: String? = null,
    val name: String? = null,
    val image: String? = null,
    val description:String? = null,
    val price:String? = null,
    val rating:String? = null,
    var sold_item_count:Int? = null,
    val CategoryID : String? = null,
)

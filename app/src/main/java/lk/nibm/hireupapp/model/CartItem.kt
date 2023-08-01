package lk.nibm.hireupapp.model

data class CartItem(
    var productId: String = "",
    var productName: String = "",
    var productPrice: String = "",
    var quantity: Int = 0,
    var productImage: String = "",
    var isSelected: Boolean = false
)

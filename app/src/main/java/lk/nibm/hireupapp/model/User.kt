package lk.nibm.hireupapp.model

data class User(
    val userId: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val mobileNumber : String? = null,
    val gender : String? = null,
    val password: String? = null
)

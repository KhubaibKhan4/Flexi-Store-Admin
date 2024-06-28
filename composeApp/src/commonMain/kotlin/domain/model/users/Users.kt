package domain.model.users


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Users(
    @SerialName("address")
    val address: String = "",
    @SerialName("city")
    val city: String = "",
    @SerialName("country")
    val country: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("fullName")
    val fullName: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("password")
    val password: String = "",
    @SerialName("phoneNumber")
    val phoneNumber: String = "",
    @SerialName("postalCode")
    val postalCode: Int = 0,
    @SerialName("profileImage")
    val profileImage: String = "",
    @SerialName("userRole")
    val userRole: String = "",
    @SerialName("username")
    val username: String = ""
)
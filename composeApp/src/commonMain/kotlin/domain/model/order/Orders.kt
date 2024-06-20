package domain.model.order


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Orders(
    @SerialName("deliveryDate")
    val deliveryDate: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("orderDate")
    val orderDate: String = "",
    @SerialName("orderProgress")
    val orderProgress: String = "",
    @SerialName("paymentType")
    val paymentType: String = "",
    @SerialName("productIds")
    val productIds: Int = 0,
    @SerialName("selectedColor")
    val selectedColor: String = "",
    @SerialName("totalPrice")
    val totalPrice: Int = 0,
    @SerialName("totalQuantity")
    val totalQuantity: String = "",
    @SerialName("trackingId")
    val trackingId: String = "",
    @SerialName("userId")
    val userId: Int = 0
)
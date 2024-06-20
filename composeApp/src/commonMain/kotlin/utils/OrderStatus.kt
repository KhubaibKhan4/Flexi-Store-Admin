package utils

enum class OrderStatus(val displayName: String) {
    ORDER_PLACED("Order Placed"),
    PACKAGING("Packaging"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered")
}

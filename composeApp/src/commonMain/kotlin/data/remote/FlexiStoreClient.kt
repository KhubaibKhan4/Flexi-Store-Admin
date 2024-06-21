package data.remote

import domain.model.order.Orders
import domain.model.products.Products
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class FlexiStoreClient(
    private val client: HttpClient,
) {
    suspend fun getAllOrders(): List<Orders> {
        return client.get("v1/order").body()
    }
    suspend fun getProductsByMultipleIds(ids: String): List<Products>{
        return client.get("v1/products/multiple?ids=$ids").body()
    }
    suspend fun getAllProducts(): List<Products>{
        return client.get("v1/products").body()
    }
}
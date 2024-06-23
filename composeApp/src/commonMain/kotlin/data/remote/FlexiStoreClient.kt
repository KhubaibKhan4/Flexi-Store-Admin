package data.remote

import domain.model.order.Orders
import domain.model.products.Products
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.util.InternalAPI

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
    @OptIn(InternalAPI::class)
    suspend fun updateProductById(
        id: Long,
        name: String?,
        description: String?,
        price: Long?,
        categoryId: Long?,
        categoryTitle: String?,
        imageBytes: ByteArray?,
        created_at: String?,
        updated_at: String?,
        total_stack: Long?,
        brand: String?,
        weight: Double?,
        dimensions: String?,
        isAvailable: Boolean?,
        discountPrice: Long?,
        promotionDescription: String?,
        averageRating: Double?,
        isFeature: Boolean?,
        manufacturer: String?,
        colors: String?
    ): HttpResponse {
        val formData = formData {
            append("id", id.toString())
            append("name", name ?: "")
            append("description", description ?: "")
            append("price", price?.toString() ?: "")
            append("categoryId", categoryId?.toString() ?: "")
            append("categoryTitle", categoryTitle ?: "")
            append("created_at", created_at ?: "")
            append("updated_at", updated_at ?: "")
            append("total_stack", total_stack?.toString() ?: "")
            append("brand", brand ?: "")
            append("weight", weight?.toString() ?: "")
            append("dimensions", dimensions ?: "")
            append("isAvailable", isAvailable?.toString() ?: "")
            append("discountPrice", discountPrice?.toString() ?: "")
            append("promotionDescription", promotionDescription ?: "")
            append("averageRating", averageRating?.toString() ?: "")
            append("isFeature", isFeature?.toString() ?: "")
            append("manufacturer", manufacturer ?: "")
            append("colors", colors ?: "")
            if (imageBytes != null) {
                append("image", imageBytes, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename={$name}product_image{$id}.jpg")
                })
            }
        }

        return client.put("v1/products/$id") {
            body = MultiPartFormDataContent(formData)
        }
    }
}
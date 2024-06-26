package data.remote

import domain.model.categories.Categories
import domain.model.order.Orders
import domain.model.products.Products
import domain.model.promotions.Promotion
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import kotlinx.datetime.Clock

class FlexiStoreClient(
    private val client: HttpClient,
) {
    suspend fun getAllOrders(): List<Orders> {
        return client.get("v1/order").body()
    }

    suspend fun getProductsByMultipleIds(ids: String): List<Products> {
        return client.get("v1/products/multiple?ids=$ids").body()
    }

    suspend fun getAllProducts(): List<Products> {
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
        colors: String?,
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
                    append(
                        HttpHeaders.ContentDisposition,
                        "form-data; name=\"image\"; filename=\"${name ?: "product_image"}.jpg\""
                    )
                    append(HttpHeaders.ContentType, "image/jpeg")
                })
            }
        }

        return client.put("v1/products/$id") {
            body = MultiPartFormDataContent(formData)
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun createProduct(
        name: String,
        description: String,
        price: Long,
        categoryId: Long,
        categoryTitle: String,
        imageBytes: ByteArray,
        createdAt: String,
        updatedAt: String,
        totalStack: Long,
        brand: String,
        weight: Double,
        dimensions: String,
        isAvailable: Boolean,
        discountPrice: Long,
        promotionDescription: String,
        averageRating: Double,
        isFeature: Boolean,
        manufacturer: String,
        colors: String,
    ): HttpResponse {
        val formData = formData {
            append("name", name)
            append("description", description)
            append("price", price.toString())
            append("categoryId", categoryId.toString())
            append("categoryTitle", categoryTitle)
            append("created_at", createdAt)
            append("updated_at", updatedAt)
            append("total_stack", totalStack.toString())
            append("brand", brand)
            append("weight", weight.toString())
            append("dimensions", dimensions)
            append("isAvailable", isAvailable.toString())
            append("discountPrice", discountPrice.toString())
            append("promotionDescription", promotionDescription)
            append("averageRating", averageRating.toString())
            append("isFeature", isFeature.toString())
            append("manufacturer", manufacturer)
            append("colors", colors)
            append("image", imageBytes, Headers.build {
                append(
                    HttpHeaders.ContentDisposition,
                    "form-data; name=\"image\"; filename=\"${name}.jpg\""
                )
                append(HttpHeaders.ContentType, "image/jpeg")
            })
        }

        return client.post("v1/products") {
            body = MultiPartFormDataContent(formData)
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun createCategories(
        name: String,
        description: String,
        isVisible: Boolean,
        imageBytes: ByteArray,

        ): HttpResponse {
        val formData = formData {
            append("name", name)
            append("description", description)
            append("isVisible", isVisible.toString())
            append("imageUrl", imageBytes, Headers.build {
                append(
                    HttpHeaders.ContentDisposition,
                    "form-data; name=\"image\"; filename=\"${name}.jpg\""
                )
                append(HttpHeaders.ContentType, "image/jpeg")
            })
        }

        return client.post("v1/categories") {
            body = MultiPartFormDataContent(formData)
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun updateCategoryById(
        id: Long,
        name: String,
        description: String,
        isVisible: Boolean,
        imageBytes: ByteArray,
    ): HttpResponse {
        val formData = formData {
            append("id", id.toString())
            append("name", name)
            append("description", description)
            append("isVisible", isVisible.toString())
            append("image", imageBytes, Headers.build {
                append(
                    HttpHeaders.ContentDisposition,
                    "form-data; name=\"image\"; filename=\"${name}.jpg\""
                )
                append(HttpHeaders.ContentType, "image/jpeg")
            })
        }

        return client.put("v1/categories/$id") {
            body = MultiPartFormDataContent(formData)
        }
    }

    suspend fun getAllCategories(): List<Categories> {
        return client.get("v1/categories").body()
    }

    @OptIn(InternalAPI::class)
    suspend fun updateOrderById(id: Long, orderProgress: String): HttpResponse {
        println("ORDER STARTED")
        return client.put("v1/order/$id") {
            contentType(ContentType.Application.FormUrlEncoded)
            body = FormDataContent(Parameters.build {
                append("orderProgress", orderProgress)
            })
        }
    }
    suspend fun getPromotions(): List<Promotion>{
        return client.get("v1/promotions").body()
    }
    suspend fun deletePromotionById(id: Long): HttpResponse {
        return client.delete("v1/promotions/$id").body()
    }
    @OptIn(InternalAPI::class)
    suspend fun createPromotion(
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        enable: Boolean,
        image: ByteArray,

        ): HttpResponse {
        val formData = formData {
            append("name", title)
            append("description", description)
            append("startDate", startDate)
            append("endDate", endDate)
            append("enable", enable)
            append("imageUrl", image, Headers.build {
                append(
                    HttpHeaders.ContentDisposition,
                    "form-data; name=\"image\"; filename=\"${title}.jpg\""
                )
                append(HttpHeaders.ContentType, "image/jpeg")
            })
        }

        return client.post("v1/promotions") {
            body = MultiPartFormDataContent(formData)
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun updatePromotionById(
        id: Long,
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        enable: String,
        imageBytes: ByteArray,
    ): HttpResponse {
        val formData = formData {
            append("id", id.toString())
            append("title", title)
            append("description", description)
            append("startDate", startDate)
            append("endDate", endDate)
            append("enable", enable)
            append("image", imageBytes, Headers.build {
                append(
                    HttpHeaders.ContentDisposition,
                    "form-data; name=\"image\"; filename=\"${title}.jpg\""
                )
                append(HttpHeaders.ContentType, "image/jpeg")
            })
        }

        return client.put("v1/promotions/$id") {
            body = MultiPartFormDataContent(formData)
        }
    }
}
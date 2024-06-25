package data.repository

import domain.model.categories.Categories
import domain.model.order.Orders
import domain.model.products.Products
import io.ktor.client.statement.HttpResponse
import io.ktor.util.InternalAPI

interface FlexiRepository {
    suspend fun getAllOrders(): List<Orders>
    suspend fun getProductsByMultipleIds(ids: String): List<Products>
    suspend fun getAllProducts(): List<Products>
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
    ): HttpResponse

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
        colors: String
    ): HttpResponse
    suspend fun getAllCategories(): List<Categories>
    @OptIn(InternalAPI::class)
    suspend fun createCategories(
        name: String,
        description: String,
        isVisible: Boolean,
        imageBytes: ByteArray,

        ): HttpResponse
}
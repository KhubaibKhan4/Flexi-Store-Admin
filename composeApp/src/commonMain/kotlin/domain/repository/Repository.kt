package domain.repository

import data.remote.FlexiStoreClient
import data.repository.FlexiRepository
import domain.model.categories.Categories
import domain.model.order.Orders
import domain.model.products.Products
import io.ktor.client.statement.HttpResponse

class Repository(
    private val flexiStoreClient: FlexiStoreClient
): FlexiRepository {
    override suspend fun getAllOrders(): List<Orders> {
        return flexiStoreClient.getAllOrders()
    }

    override suspend fun getProductsByMultipleIds(ids: String): List<Products> {
        return flexiStoreClient.getProductsByMultipleIds(ids)
    }
    override suspend fun getAllProducts(): List<Products> {
        return flexiStoreClient.getAllProducts()
    }

    override suspend fun updateProductById(
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
        return flexiStoreClient.updateProductById(id, name, description, price, categoryId, categoryTitle, imageBytes, created_at, updated_at, total_stack, brand, weight, dimensions, isAvailable, discountPrice, promotionDescription, averageRating, isFeature, manufacturer, colors)
    }

    override suspend fun createProduct(
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
        return flexiStoreClient.createProduct(name, description, price, categoryId, categoryTitle, imageBytes, createdAt, updatedAt, totalStack, brand, weight, dimensions, isAvailable, discountPrice, promotionDescription, averageRating, isFeature, manufacturer, colors)
    }

    override suspend fun getAllCategories(): List<Categories> {
        return flexiStoreClient.getAllCategories()
    }
}
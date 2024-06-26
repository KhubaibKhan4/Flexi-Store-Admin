package domain.repository

import data.remote.FlexiStoreClient
import data.repository.FlexiRepository
import domain.model.categories.Categories
import domain.model.order.Orders
import domain.model.products.Products
import domain.model.promotions.Promotion
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

    override suspend fun createCategories(
        name: String,
        description: String,
        isVisible: Boolean,
        imageBytes: ByteArray,
    ): HttpResponse {
        return flexiStoreClient.createCategories(name, description, isVisible, imageBytes)
    }

    override suspend fun updateCategoryById(
        id: Long,
        name: String,
        description: String,
        isVisible: Boolean,
        imageBytes: ByteArray,
    ): HttpResponse {
        return flexiStoreClient.updateCategoryById(id, name, description, isVisible, imageBytes)
    }

    override suspend fun updateOrderById(id: Long, orderProgress: String): HttpResponse {
        println("REPOSITORY")
        return flexiStoreClient.updateOrderById(id,orderProgress)
    }

    override suspend fun getPromotions(): List<Promotion> {
        return flexiStoreClient.getPromotions()
    }

    override suspend fun deletePromotionById(id: Long): HttpResponse {
        return flexiStoreClient.deletePromotionById(id)
    }

    override suspend fun updatePromotionById(
        id: Long,
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        enable: String,
        imageBytes: ByteArray,
    ): HttpResponse {
        return flexiStoreClient.updatePromotionById(id, title, description, startDate, endDate, enable, imageBytes)
    }

    override suspend fun createPromotion(
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        enable: Boolean,
        image: ByteArray,
    ): HttpResponse {
        return flexiStoreClient.createPromotion(title, description, startDate, endDate, enable, image)
    }
}
package domain.repository

import data.remote.FlexiStoreClient
import data.repository.FlexiRepository
import domain.model.order.Orders
import domain.model.products.Products

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
}
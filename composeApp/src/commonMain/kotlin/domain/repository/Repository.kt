package domain.repository

import data.remote.FlexiStoreClient
import data.repository.FlexiRepository
import domain.model.order.Orders

class Repository(
    private val flexiStoreClient: FlexiStoreClient
): FlexiRepository {
    override suspend fun getAllOrders(): List<Orders> {
        return flexiStoreClient.getAllOrders()
    }
}
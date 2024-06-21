package data.repository

import domain.model.order.Orders
import domain.model.products.Products

interface FlexiRepository {
    suspend fun getAllOrders(): List<Orders>
    suspend fun getProductsByMultipleIds(ids: String): List<Products>
    suspend fun getAllProducts(ids: String): List<Products>
}
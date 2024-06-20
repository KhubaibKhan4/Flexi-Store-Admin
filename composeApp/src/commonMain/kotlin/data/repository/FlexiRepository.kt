package data.repository

import domain.model.order.Orders

interface FlexiRepository {
    suspend fun getAllOrders(): List<Orders>
}
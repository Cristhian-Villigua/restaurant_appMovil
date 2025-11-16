package com.example.project_mobileapplicacion.model

import java.io.Serializable

data class OrderModel(
    val orderId: String = "",
    val items: List<ItemsModel> = emptyList(),
    val timestamp: Long = System.currentTimeMillis(),
    val tableNumber: String = "",
    val waiterName: String = ""
) : Serializable
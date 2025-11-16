package com.example.project_mobileapplicacion.helper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.project_mobileapplicacion.model.OrderHistoryItem

class OrderHistoryManager(private val context: Context) {

    private val PREFS_NAME = "OrderHistoryPrefs"
    private val HISTORY_KEY = "orders_list"
    private val ORDER_NUMBER_KEY = "next_local_order_number"
    private val gson = Gson()
    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun addOrder(orderText: String) {
        val currentList = loadHistory().toMutableList()

        // 1. Obtener y asegurar el contador secuencial para invitados
        val nextOrderNum = sharedPrefs.getInt(ORDER_NUMBER_KEY, 1)
        sharedPrefs.edit().putInt(ORDER_NUMBER_KEY, nextOrderNum + 1).apply()

        val newItem = OrderHistoryItem(
            id = nextOrderNum.toString(), // <--- ID CORTO SECUENCIAL
            orderText = orderText,
            timestamp = System.currentTimeMillis()
        )
        currentList.add(0, newItem)
        saveHistory(currentList)
    }

    fun loadHistory(): List<OrderHistoryItem> {
        val json = sharedPrefs.getString(HISTORY_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<OrderHistoryItem>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveHistory(historyList: List<OrderHistoryItem>) {
        val json = gson.toJson(historyList)
        sharedPrefs.edit().putString(HISTORY_KEY, json).apply()
    }

    fun getOrderById(orderId: String): OrderHistoryItem? {
        return loadHistory().find { it.id == orderId }
    }
}
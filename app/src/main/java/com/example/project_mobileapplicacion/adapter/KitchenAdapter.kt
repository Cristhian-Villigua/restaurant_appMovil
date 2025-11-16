package com.example.project_mobileapplicacion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.model.OrderModel
import java.text.SimpleDateFormat
import java.util.*

class KitchenAdapter : RecyclerView.Adapter<KitchenAdapter.OrderViewHolder>() {

    private val orders = ArrayList<OrderModel>()
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtOrderNumber: TextView = view.findViewById(R.id.txtOrderNumber)
        val txtOrderTime: TextView = view.findViewById(R.id.txtOrderTime)
        val txtOrderItems: TextView = view.findViewById(R.id.txtOrderItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_kitchen, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        // Número de orden (invertido para que la última sea la más alta)
        val orderNumber = orders.size - position
        holder.txtOrderNumber.text = "Orden #$orderNumber"

        // Hora de la orden
        holder.txtOrderTime.text = dateFormat.format(Date(order.timestamp))

        // Construir lista de items
        val itemsText = buildString {
            order.items.forEachIndexed { index, item ->
                append("${index + 1}. ${item.title} x${item.numberInCart}")
                if (item.extra.isNotEmpty()) {
                    append("\n   Extra: ${item.extra}")
                }
                if (index < order.items.size - 1) {
                    append("\n\n")
                }
            }
        }

        holder.txtOrderItems.text = itemsText
    }

    override fun getItemCount(): Int = orders.size

    /**
     * Agrega una orden al inicio de la lista si no existe (por orderId).
     * Notifica solo la inserción para evitar repintados completos.
     */
    fun addOrder(order: OrderModel) {
        // Evitar duplicados por orderId
        if (orders.any { it.orderId == order.orderId }) {
            // Ya existe: ignorar
            return
        }
        orders.add(0, order)
        notifyItemInserted(0)
    }

    /**
     * Agregar varias órdenes (se asume que vienen con IDs únicos).
     * Inserta al final de la lista actual (comportamiento conservador).
     */
    fun addOrders(newOrders: List<OrderModel>) {
        val toAdd = newOrders.filter { newOrder ->
            orders.none { it.orderId == newOrder.orderId }
        }
        if (toAdd.isEmpty()) return

        val startPosition = orders.size
        orders.addAll(toAdd)
        notifyItemRangeInserted(startPosition, toAdd.size)
    }

    fun clearOrders() {
        orders.clear()
        notifyDataSetChanged()
    }

    fun getAllOrders(): List<OrderModel> = orders.toList()

    fun removeOrder(position: Int) {
        if (position in orders.indices) {
            orders.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}

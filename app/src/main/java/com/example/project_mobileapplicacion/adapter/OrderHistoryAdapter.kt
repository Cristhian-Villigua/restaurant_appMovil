package com.example.project_mobileapplicacion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.model.OrderHistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryAdapter(
    private var orderList: List<OrderHistoryItem>,
    private val clickListener: (OrderHistoryItem) -> Unit
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tv_order_id)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tv_order_timestamp)

        fun bind(order: OrderHistoryItem) {
            tvOrderId.text = order.id
            tvTimestamp.text = dateFormat.format(Date(order.timestamp))

            itemView.setOnClickListener {
                clickListener(order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_history, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size

    fun updateData(newOrderList: List<OrderHistoryItem>) {
        orderList = newOrderList
        notifyDataSetChanged()
    }
}
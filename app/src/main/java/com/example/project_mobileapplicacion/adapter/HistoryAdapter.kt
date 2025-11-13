package com.example.project_mobileapplicacion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.model.Order
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private var orderList: List<Order>,
    private val onDetailsClick: (Order) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: TextView = view.findViewById(R.id.tv_order_id)
        val total: TextView = view.findViewById(R.id.tv_order_total)
        val status: TextView = view.findViewById(R.id.tv_order_status)
        val timestamp: TextView = view.findViewById(R.id.tv_order_timestamp)
        val btnDetails: Button = view.findViewById(R.id.btn_view_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val order = orderList[position]

        holder.orderId.text = order.id.toString()
        holder.total.text = "$${order.total}"
        holder.status.text = order.status.uppercase()

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.timestamp.text = sdf.format(Date(order.timestamp))

        holder.btnDetails.setOnClickListener {
            onDetailsClick(order)
        }
    }

    override fun getItemCount(): Int = orderList.size
}

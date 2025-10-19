package com.example.project_mobileapplicacion.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.model.UserEntity

class UserAdapter: ListAdapter<UserEntity, UserAdapter.VH>(Diff()) {
    class VH(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtDetail: TextView = itemView.findViewById(R.id.txtDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val u = getItem(position)
        holder.txtName.text = "${u.name} ${u.lastname}"
        holder.txtDetail.text = "Birthday: ${u.birthday} \nPhone: ${u.phone} \nEmail: ${u.email} \nPassword: ${u.password}"
    }

    private class Diff: DiffUtil.ItemCallback<UserEntity>(){
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity) = oldItem == newItem
    }
}
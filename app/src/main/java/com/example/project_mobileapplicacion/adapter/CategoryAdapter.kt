package com.example.project_mobileapplicacion.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.databinding.ActivityViewholderCategoryBinding
import com.example.project_mobileapplicacion.model.CategoryModel
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.user.ItemsListActivity

class CategoryAdapter(val items: MutableList<CategoryModel>)
    :RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
        private lateinit var context: Context
        private var selectPosition= -1
    private var lastSelectedPosition = -1
    inner class ViewHolder(val binding: ActivityViewholderCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        context=parent.context
        val binding = ActivityViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
       val item = items[position]
        holder.binding.titleCat.text = item.title
        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectPosition
            selectPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectPosition)

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(context, ItemsListActivity::class.java).apply {
                    putExtra("id", item.id.toString())
                    putExtra("title", item.title)
                }
                ContextCompat.startActivity(context,intent,null)
            },500)
        }
        if(selectPosition == position){
            holder.binding.titleCat.setBackgroundResource(R.drawable.dark_brown_bg)
            holder.binding.titleCat.setTextColor(context.resources.getColor(R.color.white))
        }else{
            holder.binding.titleCat.setBackgroundResource(R.drawable.white_bg)
            holder.binding.titleCat.setTextColor(context.resources.getColor(R.color.darkBrown))
        }
    }

    override fun getItemCount(): Int = items.size
}
package com.example.project_mobileapplicacion.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.databinding.ActivityViewholderCategoryBinding
import com.example.project_mobileapplicacion.model.CategoryModel
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.user.ItemsListFragment

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
                if (context is AppCompatActivity) {
                    val activity = context as AppCompatActivity
                    val fragment = ItemsListFragment.newInstance(item.id.toString(), item.title)
                    activity.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                        )
                        .replace(com.example.project_mobileapplicacion.R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }, 500)
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
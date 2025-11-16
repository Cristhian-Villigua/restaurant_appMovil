package com.example.project_mobileapplicacion.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.databinding.ViewholderCategoryBinding
import com.example.project_mobileapplicacion.model.CategoryModel
import com.example.project_mobileapplicacion.user.ItemsListFragment

class CategoryAdapter(
    private val items: MutableList<CategoryModel>,
    private val fragment: Fragment
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var selectPosition = -1
    private var lastSelectedPosition = -1

    inner class ViewHolder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleCat.text = item.title

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectPosition
            selectPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectPosition)

            Handler(Looper.getMainLooper()).postDelayed({
                fragment.parentFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    replace(
                        R.id.fragmentContainer,
                        ItemsListFragment.newInstance(item.id.toString(), item.title)
                    )
                    addToBackStack(null)
                }
            }, 200)
        }

        if (selectPosition == position) {
            holder.binding.titleCat.setBackgroundResource(R.drawable.dark_brown_bg)
            holder.binding.titleCat.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
        } else {
            holder.binding.titleCat.setBackgroundResource(R.drawable.white_bg)
            holder.binding.titleCat.setTextColor(
                ContextCompat.getColor(context, R.color.darkBrown)
            )
        }
    }

    override fun getItemCount(): Int = items.size
}
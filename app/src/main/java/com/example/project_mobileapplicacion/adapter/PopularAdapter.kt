package com.example.project_mobileapplicacion.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.databinding.ViewholderPopularBinding
import com.example.project_mobileapplicacion.model.ItemsModel
import com.example.project_mobileapplicacion.user.DetailFragment

class PopularAdapter(
    private val items: MutableList<ItemsModel>,
    private val fragment: Fragment
) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(val binding: ViewholderPopularBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderPopularBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.txtTitle.text = item.title
        holder.binding.txtPrice.text = "$${item.price}"

        Glide.with(context)
            .load(item.picUrl[0])
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            fragment.parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                replace(R.id.fragmentContainer, DetailFragment.newInstance(item))
                addToBackStack(null)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
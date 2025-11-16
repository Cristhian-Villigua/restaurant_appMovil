package com.example.project_mobileapplicacion.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.databinding.ViewholderItemPicLeftBinding
import com.example.project_mobileapplicacion.databinding.ViewholderItemPicRightBinding
import com.example.project_mobileapplicacion.model.ItemsModel
import com.example.project_mobileapplicacion.user.DetailFragment

class ItemsListCategoryAdapter(
    private val items: MutableList<ItemsModel>,
    private val fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ITEM1 = 0
        const val TYPE_ITEM2 = 1
    }

    private lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) TYPE_ITEM1 else TYPE_ITEM2
    }

    class ViewHolderItem1(val binding: ViewholderItemPicRightBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolderItem2(val binding: ViewholderItemPicLeftBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            TYPE_ITEM1 -> {
                val binding = ViewholderItemPicRightBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                ViewHolderItem1(binding)
            }
            TYPE_ITEM2 -> {
                val binding = ViewholderItemPicLeftBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                ViewHolderItem2(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is ViewHolderItem1 -> {
                holder.binding.txtTitle.text = item.title
                holder.binding.txtPrice.text = "${item.price} USD"
                holder.binding.ratingBar.rating = item.rating.toFloat()

                Glide.with(context)
                    .load(item.picUrl[0])
                    .into(holder.binding.picMain)

                holder.itemView.setOnClickListener {
                    navigateToDetail(item)
                }
            }
            is ViewHolderItem2 -> {
                holder.binding.txtTitle.text = item.title
                holder.binding.txtPrice.text = "${item.price} USD"
                holder.binding.ratingBar.rating = item.rating.toFloat()

                Glide.with(context)
                    .load(item.picUrl[0])
                    .into(holder.binding.picMain)

                holder.itemView.setOnClickListener {
                    navigateToDetail(item)
                }
            }
        }
    }

    private fun navigateToDetail(item: ItemsModel) {
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

    fun updateItems(newItems: List<ItemsModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
package com.example.project_mobileapplicacion.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_mobileapplicacion.databinding.ViewholderItemPicLeftBinding
import com.example.project_mobileapplicacion.databinding.ViewholderItemPicRightBinding
import com.example.project_mobileapplicacion.model.ItemsModel
import com.example.project_mobileapplicacion.user.DetailFragment
import com.example.project_mobileapplicacion.R

class ItemsListCategoryAdapter(val items: MutableList<ItemsModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        const val TYPE_ITEM1 = 0
        const val TYPE_ITEM2 = 1
    }
    lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return if(position%2==0) TYPE_ITEM1 else TYPE_ITEM2
    }
    class ViewHolderItem1(val binding: ViewholderItemPicRightBinding): RecyclerView.ViewHolder(binding.root)
    class ViewHolderItem2(val binding: ViewholderItemPicLeftBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType){
            TYPE_ITEM1->{
                val binding = ViewholderItemPicRightBinding.inflate(LayoutInflater.from(context),parent,false)
                ViewHolderItem1(binding)
            }
            TYPE_ITEM2->{
                val binding = ViewholderItemPicLeftBinding.inflate(LayoutInflater.from(context),parent,false)
                ViewHolderItem2(binding)
            }
            else->throw IllegalArgumentException("Invalid view type")
        }
    }
    override fun getItemCount(): Int= items.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        fun bindCommonData(
            txtTitle: String,
            txtPrice: String,
            rating: Float,
            picUrl: String
        ){
            when(holder){
                is ViewHolderItem1 ->{
                    holder.binding.txtTitle.text = txtTitle
                    holder.binding.txtPrice.text = txtPrice
                    holder.binding.ratingBar.rating = rating

                    Glide.with(context)
                        .load(picUrl)
                        .into(holder.binding.picMain)

                    holder.itemView.setOnClickListener {
                        navigateToDetail(items[position])
                    }
                }
                is ViewHolderItem2 ->{
                    holder.binding.txtTitle.text = txtTitle
                    holder.binding.txtPrice.text = txtPrice
                    holder.binding.ratingBar.rating = rating

                    Glide.with(context)
                        .load(picUrl)
                        .into(holder.binding.picMain)

                    holder.itemView.setOnClickListener {
                        navigateToDetail(items[position])
                    }
                }
            }
        }

        bindCommonData(
            item.title,
            "${item.price} USD",
            item.rating.toFloat(),
            item.picUrl[0]
        )
    }

    // Función para navegar al DetailFragment
    private fun navigateToDetail(item: ItemsModel) {
        if (context is AppCompatActivity) {
            val activity = context as AppCompatActivity
            val fragment = DetailFragment.newInstance(item)

            activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
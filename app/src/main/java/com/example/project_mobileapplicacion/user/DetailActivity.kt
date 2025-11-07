package com.example.project_mobileapplicacion.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.project_mobileapplicacion.MainActivity
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.databinding.ActivityDetailBinding
import com.example.project_mobileapplicacion.helper.ManagmentCart
import com.example.project_mobileapplicacion.model.ItemsModel

class DetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managementCart: ManagmentCart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)
        bundle()

    }

    private fun bundle(){
        binding.apply {
            item = intent.getSerializableExtra("object") as ItemsModel
            Glide.with(this@DetailActivity)
                .load(item.picUrl[0])
                .into(binding.picMain)

            textTitle.text = item.title
            txtDescription.text = item.description
            txtPrice.text = "$"+item.price
            txtRating.text = item.rating.toString()

            btnAddToCart.setOnClickListener {
                item.numberInCart = Integer.valueOf(
                    itemNumber.text.toString()
                )
                managementCart.insertItems(item)
            }
            btnBack.setOnClickListener {
                startActivity(Intent(this@DetailActivity, MainActivity::class.java))
            }
            btnPlus.setOnClickListener {
                itemNumber.text = (item.numberInCart+1).toString()
                item.numberInCart++
            }
            btnMinus.setOnClickListener {
                if(item.numberInCart>0){
                    itemNumber.text = (item.numberInCart-1).toString()
                    item.numberInCart--
                }
            }
        }
    }
}
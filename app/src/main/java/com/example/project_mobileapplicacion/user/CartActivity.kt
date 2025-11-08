package com.example.project_mobileapplicacion.user

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.adapter.CartAdapter
import com.example.project_mobileapplicacion.databinding.ActivityCartBinding
import com.example.project_mobileapplicacion.helper.ChangeNumberItemsListener
import com.example.project_mobileapplicacion.helper.ManagmentCart

class CartActivity : AppCompatActivity() {
    lateinit var binding: ActivityCartBinding
    lateinit var managementCart: ManagmentCart
    private var tax: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)
        calculateCart()
        setVariable()
        initCartList()
    }



    private fun initCartList() {
        binding.apply {
            listView.layoutManager =
                LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            listView.adapter= CartAdapter(
                managementCart.getListCart(),
                this@CartActivity, object : ChangeNumberItemsListener{
                    override fun onChanged() {
                        calculateCart()
                    }
                }
            )
        }
    }

    private fun setVariable() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 15
        tax = Math.round((managementCart.getTotalFee()*percentTax)*100)/100.0
        val total = Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100
        val itemTotal = Math.round(managementCart.getTotalFee()*100)/100
        binding.apply {
            txtTotalFee.text = "$$itemTotal"
            txtTotalTax.text = "$$tax"
            txtDelivery.text = "$$delivery"
            txtTotal.text = "$$total"
        }
    }
}
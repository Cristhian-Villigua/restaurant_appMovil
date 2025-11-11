package com.example.project_mobileapplicacion.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.adapter.CartAdapter
import com.example.project_mobileapplicacion.databinding.FragmentCartBinding
import com.example.project_mobileapplicacion.helper.ChangeNumberItemsListener
import com.example.project_mobileapplicacion.helper.ManagmentCart
import com.example.project_mobileapplicacion.menu.KitchenActivity
import com.example.project_mobileapplicacion.model.ItemsModel
import org.json.JSONArray
import org.json.JSONObject

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var managementCart: ManagmentCart
    private var tax: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        managementCart = ManagmentCart(requireContext())
        calculateCart()
        initCartList()
        configureTopBar()

        val btnOrder = view.findViewById<Button>(R.id.btnOrder)
        btnOrder.setOnClickListener {
            val cartItems: ArrayList<ItemsModel> = managementCart.getListCart()
            if (cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jsonArray = JSONArray()
            for (item in cartItems) {
                val obj = JSONObject()
                obj.put("title", item.title)
                obj.put("description", item.description)
                val pics = JSONArray()
                for (url in item.picUrl) pics.put(url)
                obj.put("picUrl", pics)
                obj.put("price", item.price)
                obj.put("rating", item.rating)
                obj.put("numberInCart", item.numberInCart)
                obj.put("extra", item.extra)
                jsonArray.put(obj)
            }

            val jsonString = jsonArray.toString()
            val intent = Intent(requireContext(), QrActivity::class.java)
            intent.putExtra("ORDER_DETAILS", jsonString)
            startActivity(intent)
        }
    }

    private fun configureTopBar() {
        activity?.let { act ->
            val btnLeftBartTop = act.findViewById<ImageView>(R.id.btnLeftBarTop)
            val btnRightBarTop = act.findViewById<ImageView>(R.id.btnRightBarTop)
            val name = act.findViewById<TextView>(R.id.nameBarTop)

            btnLeftBartTop?.apply {
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }

            btnRightBarTop?.apply {
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }
            name?.text = getString(R.string.Cart)
        }
    }

    private fun initCartList() {
        binding.apply {
            listView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            listView.adapter = CartAdapter(
                managementCart.getListCart(),
                requireContext(),
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        calculateCart()
                    }
                }
            )
        }
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 15
        tax = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100.0
        val total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100
        val itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100

        binding.apply {
            txtTotalFee.text = "$$itemTotal"
            txtTotalTax.text = "$$tax"
            txtDelivery.text = "$$delivery"
            txtTotal.text = "$$total"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }
}
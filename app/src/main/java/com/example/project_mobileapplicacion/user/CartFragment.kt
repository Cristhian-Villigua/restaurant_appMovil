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
import com.example.project_mobileapplicacion.model.ItemsModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
            val btnLeft = act.findViewById<ImageView>(R.id.btnLeftBarTop)
            val btnRight = act.findViewById<ImageView>(R.id.btnRightBarTop)
            val name = act.findViewById<TextView>(R.id.nameBarTop)
            val tilSearch = act.findViewById<TextInputLayout>(R.id.tilSearch)
            val etSearch = act.findViewById<TextInputEditText>(R.id.etSearch)

            btnLeft?.apply {
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }

            btnRight?.apply {
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }

            name?.visibility = View.VISIBLE
            name?.text = getString(R.string.Cart)
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
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
        tax = (managementCart.getTotalFee() * percentTax)
        val total = managementCart.getTotalFee() + tax
        val itemTotal = managementCart.getTotalFee()

        val roundedTax = Math.round(tax * 100) / 100.0
        val roundedTotal = Math.round(total * 100) / 100.0
        val roundedItemTotal = Math.round(itemTotal * 100) / 100.0

        binding.apply {
            txtTotalFee.text = "$$roundedItemTotal"
            txtTotalTax.text = "$$roundedTax"
            txtTotal.text = "$$roundedTotal"
        }
    }
    override fun onResume() {
        super.onResume()
        initCartList()
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
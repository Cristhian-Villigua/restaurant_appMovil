package com.example.project_mobileapplicacion.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.model.ItemsModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray

class KitchenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_kitchen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        configureTopBar()

        super.onViewCreated(view, savedInstanceState)

        val orderTextView = view.findViewById<TextView>(R.id.txtOrderDetails)

        // Recuperar datos usando arguments en vez de intent
        @Suppress("UNCHECKED_CAST")
        var orderItems = arguments?.getSerializable("ORDER_ITEMS") as? ArrayList<ItemsModel>

        if (orderItems == null) {
            val details =
                arguments?.getString("ORDER_DETAILS")
                    ?: arguments?.getString("QR_CONTENT")

            if (!details.isNullOrEmpty()) {
                try {
                    val arr = JSONArray(details)
                    val list = ArrayList<ItemsModel>()

                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)

                        val title = obj.optString("title", "")
                        val numberInCart = obj.optInt("numberInCart", 1)
                        val extra = obj.optString("extra", "")

                        val item = ItemsModel(
                            title = title,
                            numberInCart = numberInCart,
                            extra = extra
                        )

                        list.add(item)
                    }

                    if (list.isNotEmpty()) orderItems = list

                } catch (e: Exception) {
                    orderTextView.text = details
                }
            }
        }

        if (orderItems == null || orderItems.isEmpty()) {
            val fallback =
                arguments?.getString("QR_CONTENT")
                    ?: arguments?.getString("ORDER_DETAILS")

            orderTextView.text = fallback ?: "No hay detalles de la orden"
            return
        }

        val sb = StringBuilder()
        sb.append("Orden recibida:\n\n")
        orderItems.forEachIndexed { index, item ->
            sb.append("${index + 1}. ${item.title} x${item.numberInCart}\n")
        }
        sb.append("\n-- Para cocina: preparar los platos indicados")

        orderTextView.text = sb.toString()
    }

    private fun configureTopBar() {
        activity?.let { act ->
            val btnLeft = act.findViewById<ImageView>(R.id.btnLeftBarTop)
            val btnRight = act.findViewById<ImageView>(R.id.btnRightBarTop)
            val name = act.findViewById<TextView>(R.id.nameBarTop)
            val tilSearch = act.findViewById<TextInputLayout>(R.id.tilSearch)
            val etSearch = act.findViewById<TextInputEditText>(R.id.etSearch)

            btnLeft?.apply {
                setImageResource(R.drawable.ic_left_arrow)
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }

            btnRight?.apply {
                setImageResource(R.drawable.ic_left_arrow)
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }

            name?.visibility = View.VISIBLE
            name?.text = getString(R.string.kitchen)
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
        }
    }

    companion object {

        fun newInstance(): KitchenFragment {
            return KitchenFragment()
        }

        fun newInstance(bundle: Bundle): KitchenFragment {
            val frag = KitchenFragment()
            frag.arguments = bundle
            return frag
        }
    }
}
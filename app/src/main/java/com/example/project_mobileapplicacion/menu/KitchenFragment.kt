package com.example.project_mobileapplicacion.menu

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.model.ItemsModel
import com.example.project_mobileapplicacion.websocket.KitchenWebSocketListener
import com.example.project_mobileapplicacion.websocket.WebSocketManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONObject

class KitchenFragment : Fragment() {

    private lateinit var webSocketManager: WebSocketManager
    private lateinit var orderTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kitchen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureTopBar()
        orderTextView = view.findViewById(R.id.txtOrderDetails)
        webSocketManager = WebSocketManager(
            KitchenWebSocketListener { message ->
                displayOrder(message)
            }
        )
        webSocketManager.connect("ws://192.168.0.107:8080")
        val roleJson = JSONObject()
        roleJson.put("role", "Cocinero")
        webSocketManager.sendMessage(roleJson.toString())

        val initialOrder = arguments?.getString("ORDER_DETAILS")
            ?: arguments?.getString("QR_CONTENT")
            ?: requireActivity().getSharedPreferences("OrdersPrefs", 0)
                .getString("LAST_ORDER", "")

        initialOrder?.let {
            displayOrder(it)
        }
    }
    private fun displayOrder(details: String) {
        if (details.isEmpty()) return
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            try {
                val arr = JSONArray(details)
                val list = ArrayList<ItemsModel>()

                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    val title = obj.optString("title", "")
                    val numberInCart = obj.optInt("numberInCart", 1)
                    val extra = obj.optString("extra", "")
                    val item = ItemsModel( title = title, numberInCart = numberInCart, extra = extra )
                    list.add(item)
                }

                if (list.isNotEmpty()) {
                    val sb = StringBuilder()
                    sb.append("Orden recibida:\n\n")
                    list.forEachIndexed { index, item ->
                        sb.append("${index + 1}. ${item.title} x${item.numberInCart}\n")
                    }
                    sb.append("\n-- Para cocina: preparar los platos indicados")
                    orderTextView.text = sb.toString()
                } else {
                    orderTextView.text = details
                }

            } catch (e: Exception) {
                orderTextView.text = details
            }
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

    override fun onDestroy() {
        super.onDestroy()
        webSocketManager.close()
    }
    companion object {
        fun newInstance(): KitchenFragment = KitchenFragment()
        fun newInstance(bundle: Bundle): KitchenFragment {
            val frag = KitchenFragment()
            frag.arguments = bundle
            return frag
        }
    }
}
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.adapter.KitchenAdapter
import com.example.project_mobileapplicacion.model.ItemsModel
import com.example.project_mobileapplicacion.model.OrderModel
import com.example.project_mobileapplicacion.websocket.KitchenWebSocketListener
import com.example.project_mobileapplicacion.websocket.WebSocketManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class KitchenFragment : Fragment() {

    private lateinit var webSocketManager: WebSocketManager
    private lateinit var orderTextView: TextView

    // RecyclerView y Adapter (no static/shared)
    private lateinit var recyclerView: RecyclerView
    private lateinit var kitchenAdapter: KitchenAdapter

    // Contador de órdenes (solo para mostrar en logs/uso opcional)
    private var orderCounter = 0

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
        recyclerView = view.findViewById(R.id.rvOrderKitchen)

        // Configurar RecyclerView y adapter nuevo por instancia de fragment
        setupRecyclerView()

        // BOTÓN TEMPORAL DE PRUEBA (ELIMINAR DESPUÉS)
        view.findViewById<View>(R.id.rvOrderKitchen)?.setOnLongClickListener {
            val testOrder = OrderModel(
                orderId = UUID.randomUUID().toString(),
                items = listOf(
                    ItemsModel(title = "Plato de prueba", numberInCart = 1, extra = "")
                ),
                timestamp = System.currentTimeMillis()
            )
            kitchenAdapter.addOrder(testOrder)
            recyclerView.smoothScrollToPosition(0)
            updateTopBarTitle()
            println("🧪 TEST: Orden agregada. Total: ${kitchenAdapter.itemCount}")
            true
        }

        // WebSocket Manager
        webSocketManager = WebSocketManager(
            KitchenWebSocketListener { message -> displayOrder(message) }
        )
        webSocketManager.connect("ws://192.168.0.107:8080")

        // Enviar rol al servidor
        val roleJson = JSONObject()
        roleJson.put("role", "Cocinero")
        webSocketManager.sendMessage(roleJson.toString())

        // Cargar orden inicial SOLO si es la primera creación (evitar duplicados en recreaciones)
        val initialOrder = arguments?.getString("ORDER_DETAILS")
            ?: arguments?.getString("QR_CONTENT")
            ?: requireActivity().getSharedPreferences("OrdersPrefs", 0)
                .getString("LAST_ORDER", "")

        if (savedInstanceState == null && !initialOrder.isNullOrEmpty()) {
            // Solo si no venimos de recreación del fragmento
            displayOrder(initialOrder)
        }
    }

    private fun setupRecyclerView() {
        kitchenAdapter = KitchenAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = kitchenAdapter
            setHasFixedSize(true)
        }
        println("🎯 ADAPTER INICIALIZADO - Total órdenes: ${kitchenAdapter.itemCount}")
    }

    /**
     * details: JSON array de items o texto plano en caso de fallback.
     */
    private fun displayOrder(details: String) {
        if (details.isEmpty()) return

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            try {
                val arr = JSONArray(details)
                val itemsList = ArrayList<ItemsModel>()

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
                    itemsList.add(item)
                }

                if (itemsList.isNotEmpty()) {
                    orderCounter++
                    // Si el JSON incluye un orderId en el primer objeto (opcional), úsalo; si no, genera uno.
                    // (Esta lógica permite prevenir duplicados si el emisor manda un ID).
                    var parsedOrderId: String? = null
                    try {
                        // Intento de lectura opcional de un orderId raíz (si el emisor lo envía en details como objeto adicional)
                        val possibleRoot = JSONObject(details)
                        if (possibleRoot.has("orderId")) {
                            parsedOrderId = possibleRoot.optString("orderId", null)
                        }
                    } catch (_: Exception) {
                        // details es array de items, no objeto root — está bien
                    }

                    val order = OrderModel(
                        orderId = parsedOrderId ?: UUID.randomUUID().toString(),
                        items = itemsList,
                        timestamp = System.currentTimeMillis()
                    )

                    println("🔥 DEBUG: Agregando orden #$orderCounter con ${itemsList.size} items")
                    println("🔥 DEBUG: Total órdenes antes: ${kitchenAdapter.itemCount}")

                    kitchenAdapter.addOrder(order)

                    recyclerView.smoothScrollToPosition(0)

                    println("🔥 DEBUG: Total órdenes después: ${kitchenAdapter.itemCount}")

                    orderTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    updateTopBarTitle()
                } else {
                    showEmptyMessage()
                }
            } catch (e: Exception) {
                // Si es texto plano o JSON inválido, mostrarlo como fallback
                e.printStackTrace()
                showTextFallback(details)
            }
        }
    }

    private fun updateTopBarTitle() {
        activity?.let { act ->
            val name = act.findViewById<TextView>(R.id.nameBarTop)
            val orderCount = kitchenAdapter.itemCount
            name?.text = if (orderCount > 0) {
                "Cocina - Órdenes: $orderCount"
            } else {
                getString(R.string.kitchen)
            }
        }
    }

    private fun showEmptyMessage() {
        recyclerView.visibility = View.GONE
        orderTextView.visibility = View.VISIBLE
        orderTextView.text = "Esperando órdenes..."
    }

    private fun showTextFallback(text: String) {
        recyclerView.visibility = View.GONE
        orderTextView.visibility = View.VISIBLE
        orderTextView.text = text
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
        try {
            webSocketManager.close()
        } catch (e: Exception) {
            // Ignorar cierre si ya fue cerrado
            e.printStackTrace()
        }
    }

    companion object {
        const val TAG = "KitchenFragment"

        fun newInstance(): KitchenFragment = KitchenFragment()

        fun newInstance(bundle: Bundle): KitchenFragment {
            val frag = KitchenFragment()
            frag.arguments = bundle
            return frag
        }

        // Si en algún punto quieres limpiar órdenes desde fuera:
        // KitchenFragment.clearAllOrders(fragmentManager)
        fun clearAllOrdersOn(fragment: KitchenFragment) {
            fragment.kitchenAdapter.clearOrders()
        }
    }
}

package com.example.project_mobileapplicacion.menu

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.model.ItemsModel
import org.json.JSONArray

class KitchenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kitchen)

        val orderTextView = findViewById<TextView>(R.id.txtOrderDetails)

        @Suppress("UNCHECKED_CAST")
        var orderItems = intent.getSerializableExtra("ORDER_ITEMS") as? ArrayList<ItemsModel>

        if (orderItems == null) {
            val details = intent.getStringExtra("ORDER_DETAILS") ?: intent.getStringExtra("QR_CONTENT")
            if (!details.isNullOrEmpty()) {
                try {
                    val arr = JSONArray(details)
                    val list = ArrayList<ItemsModel>()
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        val title = obj.optString("title", "")
                        val numberInCart = obj.optInt("numberInCart", 1)
                        val extra = obj.optString("extra", "")

                        val pics = ArrayList<String>()
                        val picArr = obj.optJSONArray("picUrl")
                        if (picArr != null) {
                            for (j in 0 until picArr.length()) {
                                pics.add(picArr.optString(j))
                            }
                        }

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
            val fallback = intent.getStringExtra("QR_CONTENT") ?: intent.getStringExtra("ORDER_DETAILS")
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
}

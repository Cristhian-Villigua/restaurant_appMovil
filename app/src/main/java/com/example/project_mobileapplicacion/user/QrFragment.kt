package com.example.project_mobileapplicacion.user

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.helper.CartManager
import com.example.project_mobileapplicacion.helper.OrderHistoryManager
import com.example.project_mobileapplicacion.helper.SessionHelper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class QrFragment : Fragment(R.layout.fragment_qr) {

    private lateinit var qrImageView: ImageView
    private lateinit var btnOk: Button
    private lateinit var btnCancel : Button
    private lateinit var managementCart: CartManager
    private lateinit var orderHistoryManager: OrderHistoryManager
    private val ARG_ORDER_DETAILS = "ORDER_DETAILS"
    private val ARG_IS_HISTORY = "IS_HISTORY"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qrImageView = view.findViewById(R.id.qrImageView)
        btnOk = view.findViewById(R.id.btnOk)
        btnCancel = view.findViewById(R.id.btnCancel)

        managementCart = CartManager(requireContext())
        orderHistoryManager = OrderHistoryManager(requireContext())

        val orderDetails = arguments?.getString(ARG_ORDER_DETAILS)
        val isHistory = arguments?.getBoolean(ARG_IS_HISTORY, false) ?: false

        if (orderDetails.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No hay detalles de la orden para generar el QR", Toast.LENGTH_SHORT).show()
            goBack()
            return
        }

        if (orderDetails.length > 800) {
            Toast.makeText(requireContext(), "El contenido es muy largo para un QR; considera usar un ID de orden", Toast.LENGTH_LONG).show()
        }

        val qrBitmap = generateQRCode(orderDetails)
        if (qrBitmap != null) {
            qrImageView.setImageBitmap(qrBitmap)
            qrImageView.contentDescription = "Código QR de la orden"
        } else {
            Toast.makeText(requireContext(), "Error al generar el código QR", Toast.LENGTH_SHORT).show()
        }

        configureTopBar(isHistory)
        configureButtons(orderDetails, isHistory)
    }

    private fun configureTopBar(isHistory: Boolean) {
        activity?.let { act ->
            val back = act.findViewById<ImageView>(R.id.btnLeftBarTop)
            val btnRight = act.findViewById<ImageView>(R.id.btnRightBarTop)
            val name = act.findViewById<TextView>(R.id.nameBarTop)
            val tilSearch = act.findViewById<TextInputLayout>(R.id.tilSearch)
            val etSearch = act.findViewById<TextInputEditText>(R.id.etSearch)

            back?.apply {
                setImageResource(R.drawable.ic_left_arrow)
                visibility = View.VISIBLE
                isClickable = true
                isFocusable = true
                setOnClickListener { goBack() }
            }

            btnRight?.apply {
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }

            name?.visibility = View.VISIBLE
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE

            if (isHistory) {
                name?.text = "Detalles del Pedido"
            } else {
                name?.text = "Generar QR"
            }
        }
    }

    private fun configureButtons(orderDetails: String, isHistory: Boolean) {
        if (isHistory) {
            btnOk.visibility = View.GONE
            btnCancel.visibility = View.GONE
        } else {
            btnOk.visibility = View.VISIBLE
            btnCancel.visibility = View.VISIBLE
        }

        btnOk.setOnClickListener {
            val context = requireContext()
            val userId = SessionHelper.getCurrentUserId(context)

            if (!isHistory) {
                if (SessionHelper.isUserLoggedIn(context) && userId != null) {
                    FirebaseService.storeOrder(userId, orderDetails)
                    Toast.makeText(context, "Orden enviada al historial (Online)", Toast.LENGTH_SHORT).show()
                } else {
                    orderHistoryManager.addOrder(orderDetails)
                    Toast.makeText(context, "Orden enviada al historial (Local)", Toast.LENGTH_SHORT).show()
                }

                managementCart.clearCart()
            }
            goBack()
        }

        btnCancel.setOnClickListener {
            Toast.makeText(requireContext(), "Operación cancelada", Toast.LENGTH_SHORT).show()
            goBack()
        }
    }

    private fun generateQRCode(text: String?): Bitmap? {
        if (text.isNullOrEmpty()) return null
        val writer = MultiFormatWriter()
        val hints = hashMapOf<EncodeHintType, Any>()
        hints[EncodeHintType.MARGIN] = 4
        return try {
            val matrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512, hints)
            toBitmap(matrix)
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun toBitmap(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    private fun goBack() {
        parentFragmentManager.popBackStack()
    }

    companion object {
        private const val ARG_ORDER_DETAILS = "ORDER_DETAILS"
        private const val ARG_IS_HISTORY = "IS_HISTORY"

        fun newInstance(orderDetails: String, isHistory: Boolean = false): QrFragment {
            val fragment = QrFragment()
            val args = Bundle()
            args.putString(ARG_ORDER_DETAILS, orderDetails)
            args.putBoolean(ARG_IS_HISTORY, isHistory)
            fragment.arguments = args
            return fragment
        }
    }
}
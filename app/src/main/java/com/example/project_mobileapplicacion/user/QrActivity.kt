package com.example.project_mobileapplicacion.user

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.helper.ManagmentCart
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class QrActivity : AppCompatActivity() {
    private lateinit var qrImageView: ImageView
    private lateinit var btnOk: Button
    private lateinit var btnCancel : Button
    private lateinit var managementCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        qrImageView = findViewById(R.id.qrImageView)
        btnOk = findViewById(R.id.btnOk)
        btnCancel = findViewById(R.id.btnCancel)

        managementCart = ManagmentCart(this)

        val orderDetails = intent.getStringExtra("ORDER_DETAILS")
        if (orderDetails.isNullOrEmpty()) {
            Toast.makeText(this, "No hay detalles de la orden para generar el QR", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (orderDetails.length > 800) {
            Toast.makeText(this, "El contenido es muy largo para un QR; considera usar un ID de orden", Toast.LENGTH_LONG).show()
        }

        val qrBitmap = generateQRCode(orderDetails)
        if (qrBitmap != null) {
            qrImageView.setImageBitmap(qrBitmap)
            qrImageView.contentDescription = "Código QR de la orden"
        } else {
            Toast.makeText(this, "Error al generar el código QR", Toast.LENGTH_SHORT).show()
        }
        btnOk.setOnClickListener {
            managementCart.clearCart()
            Toast.makeText(this, "Orden enviada", Toast.LENGTH_SHORT).show()
            finish()
        }
        btnCancel.setOnClickListener {
            Toast.makeText(this, "Orden cancelada", Toast.LENGTH_SHORT).show()
            finish()
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
}

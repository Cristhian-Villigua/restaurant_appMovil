package com.example.project_mobileapplicacion.waiter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_mobileapplicacion.menu.KitchenActivity
import com.google.zxing.integration.android.IntentIntegrator

class ScanQrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val orderDetails = result.contents
                Toast.makeText(this, "Orden enviada", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, KitchenActivity::class.java)
                intent.putExtra("ORDER_DETAILS", orderDetails)
                startActivity(intent)
                finish()
            }
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

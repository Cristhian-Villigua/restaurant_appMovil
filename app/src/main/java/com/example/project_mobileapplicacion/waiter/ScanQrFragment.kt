package com.example.project_mobileapplicacion.waiter

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.websocket.MyWebSocketListener
import com.example.project_mobileapplicacion.websocket.WebSocketManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject

class ScanQrFragment : Fragment() {
    private lateinit var webSocketManager: WebSocketManager
    private val CAMERA_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_scan_qr, container, false)

        val btnScanQR = view.findViewById<Button>(R.id.btnScanQR)

        btnScanQR.setOnClickListener {
            checkCameraPermissionAndStart()
        }

        return view
    }

    private fun checkCameraPermissionAndStart() {
        val permission = android.Manifest.permission.CAMERA
        val granted = PackageManager.PERMISSION_GRANTED

        if (ContextCompat.checkSelfPermission(requireContext(), permission) == granted) {
            startScanner()
        } else {
            requestPermissions(arrayOf(permission), CAMERA_REQUEST_CODE)
        }
    }

    private fun startScanner() {
        IntentIntegrator.forSupportFragment(this)
            .setCaptureActivity(PortraitCaptureActivity::class.java)
            .setOrientationLocked(true)
            .initiateScan()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner()
            } else {
                Toast.makeText(requireContext(), "Se necesita el permiso de cámara", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureTopBar()
        super.onViewCreated(view, savedInstanceState)
        webSocketManager = WebSocketManager(MyWebSocketListener())
        webSocketManager.connect("ws://192.168.0.107:8080")
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
            name?.text = getString(R.string.waiter)
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null && result.contents != null) {
            val orderDetails = result.contents

            val orderJson = JSONObject()
            orderJson.put("role", "Mesero")
            orderJson.put("order", orderDetails)
            webSocketManager.sendMessage(orderJson.toString())

            Toast.makeText(requireContext(), "Orden enviada a cocina", Toast.LENGTH_SHORT).show()

            val sharedPref = requireActivity().getSharedPreferences("OrdersPrefs", 0)
            sharedPref.edit().putString("LAST_ORDER", orderDetails).apply()

            val bundle = Bundle().apply { putString("ORDER_DETAILS", orderDetails) }
            val currentFragment = ScanQrFragment.newInstance()
            currentFragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, currentFragment)
                .addToBackStack(null)
                .commit()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        webSocketManager.close()
    }


    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    companion object {
        fun newInstance(): ScanQrFragment = ScanQrFragment()
    }
}

package com.example.project_mobileapplicacion.waiter

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
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.menu.KitchenFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.integration.android.IntentIntegrator

class ScanQrFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_scan_qr, container, false)

        val btnScanQR = view.findViewById<Button>(R.id.btnScanQR)

        btnScanQR.setOnClickListener {
            IntentIntegrator.forSupportFragment(this)
                .setOrientationLocked(false)
                .initiateScan()
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureTopBar()
        super.onViewCreated(view, savedInstanceState)
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

        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            } else {
                val orderDetails = result.contents
                Toast.makeText(requireContext(), "Orden enviada", Toast.LENGTH_SHORT).show()

                val bundle = Bundle().apply {
                    putString("ORDER_DETAILS", orderDetails)
                }

                val kitchenFragment = KitchenFragment.newInstance(bundle)

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, kitchenFragment)
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        fun newInstance(): ScanQrFragment = ScanQrFragment()
    }
}

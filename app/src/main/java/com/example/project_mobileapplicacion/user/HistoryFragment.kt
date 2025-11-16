package com.example.project_mobileapplicacion.user

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.adapter.OrderHistoryAdapter
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.model.OrderHistoryItem
import com.example.project_mobileapplicacion.helper.OrderHistoryManager
import com.example.project_mobileapplicacion.helper.SessionHelper
import com.example.project_mobileapplicacion.menu.KitchenFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var rvOrderHistory: RecyclerView
    private lateinit var txtEmptyHistory: TextView
    private lateinit var progressBarHistory: ProgressBar
    private lateinit var historyManager: OrderHistoryManager
    private lateinit var historyAdapter: OrderHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvOrderHistory = view.findViewById(R.id.rvOrderHistory)
        txtEmptyHistory = view.findViewById(R.id.txtEmptyHistory)
        progressBarHistory = view.findViewById(R.id.progressBarHistory)

        historyManager = OrderHistoryManager(requireContext())

        configureRecyclerView()
        configureTopBar()
    }

    override fun onResume() {
        super.onResume()
        loadHistory()
    }

    private fun configureRecyclerView() {
        historyAdapter = OrderHistoryAdapter(emptyList()) { orderItem ->
            openQrActivityWithOrder(orderItem)
        }

        rvOrderHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun loadHistory() {
        progressBarHistory.visibility = View.VISIBLE
        val context = requireContext()
        val userId = SessionHelper.getCurrentUserId(context)

        if (SessionHelper.isUserLoggedIn(context) && userId != null) {
            FirebaseService.getOrdersByUserId(userId) { historyList ->
                activity?.runOnUiThread {
                    updateUI(historyList)
                }
            }
        } else {
            val historyList = historyManager.loadHistory()
            updateUI(historyList)
        }

    }

    private fun updateUI(historyList: List<OrderHistoryItem>) {
        historyAdapter.updateData(historyList)

        if (historyList.isEmpty()) {
            txtEmptyHistory.visibility = View.VISIBLE
            rvOrderHistory.visibility = View.GONE
        } else {
            txtEmptyHistory.visibility = View.GONE
            rvOrderHistory.visibility = View.VISIBLE
        }

        progressBarHistory.visibility = View.GONE
    }

    private fun openQrActivityWithOrder(orderItem: OrderHistoryItem) {
        val qrFragment = QrFragment.newInstance(
            orderDetails = orderItem.orderText,
            isHistory = true
        )

        parentFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            replace(R.id.fragmentContainer, qrFragment)
            addToBackStack(null)
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
            name?.text = getString(R.string.history)
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}
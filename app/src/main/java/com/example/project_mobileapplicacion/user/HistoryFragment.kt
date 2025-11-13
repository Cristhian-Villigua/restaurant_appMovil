package com.example.project_mobileapplicacion.user

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.project_mobileapplicacion.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HistoryFragment : Fragment(R.layout.fragment_history) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureTopBar()
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
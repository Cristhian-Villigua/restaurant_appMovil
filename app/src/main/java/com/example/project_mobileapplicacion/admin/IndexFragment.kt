package com.example.project_mobileapplicacion.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.project_mobileapplicacion.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class IndexFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_index, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        configureTopBar()

        super.onViewCreated(view, savedInstanceState)
        val btnVerRegister = view.findViewById<Button>(R.id.btnVerRegister)
        val btnAdmin = view.findViewById<Button>(R.id.btnAdmin)

        btnVerRegister.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainer, ListFragment.newInstance())
                setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                addToBackStack(null)
            }
        }

        btnAdmin.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainer, IndexFragment.newInstance())
                addToBackStack(null)
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
            name?.text = getString(R.string.Admin)
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): IndexFragment {
            return IndexFragment()
        }
    }
}
package com.example.project_mobileapplicacion.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.adapter.UserAdapter
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.database.AppDataBase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var btnRoom: Button
    private lateinit var btnFirebase: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureTopBar()

        rv = view.findViewById(R.id.rvUser)
        btnRoom = view.findViewById(R.id.btnRoom)
        btnFirebase = view.findViewById(R.id.btnFirebase)

        adapter = UserAdapter()
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        val db = AppDataBase.getInstance(requireContext()).userDao()

        btnRoom.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    db.getAll().collectLatest { list ->
                        adapter.submitList(list)
                    }
                }
            }
        }

        btnFirebase.setOnClickListener {
            FirebaseService.index { listFirestore ->
                adapter.submitList(listFirestore)
            }
        }
    }

    private fun configureTopBar() {
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
                setImageResource(R.drawable.ic_logout)
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }

            name?.visibility = View.VISIBLE
            name?.text = getString(R.string.Data)
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
        }
    }

    private fun goBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }
}
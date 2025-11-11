package com.example.project_mobileapplicacion.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.adapter.ItemsListCategoryAdapter
import com.example.project_mobileapplicacion.databinding.FragmentItemsListBinding
import com.example.project_mobileapplicacion.viewModel.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ItemsListFragment : Fragment() {

    private var _binding: FragmentItemsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel = MainViewModel()
    private var categoryId: String = ""
    private var categoryTitle: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        configureTopBar()
        initList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CATEGORY_ID = "categoryId"
        private const val ARG_CATEGORY_TITLE = "categoryTitle"

        fun newInstance(categoryId: String, categoryTitle: String): ItemsListFragment {
            val fragment = ItemsListFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_CATEGORY_ID, categoryId)
                putString(ARG_CATEGORY_TITLE, categoryTitle)
            }
            return fragment
        }
    }

    private fun configureTopBar() {
        activity?.let { act ->
            val btnBack = act.findViewById<ImageView>(R.id.btnLeftBarTop)
            val btnFavorite = act.findViewById<ImageView>(R.id.btnRightBarTop)
            val name = act.findViewById<TextView>(R.id.nameBarTop)
            val tilSearch = act.findViewById<TextInputLayout>(R.id.tilSearch)
            val etSearch = act.findViewById<TextInputEditText>(R.id.etSearch)

            btnBack?.apply {
                setImageResource(R.drawable.ic_left_arrow)
                visibility = View.VISIBLE
                isClickable = true
                isFocusable = true
                setOnClickListener { parentFragmentManager.popBackStack() }
            }

            btnFavorite?.apply {
                setImageResource(R.drawable.ic_favorite_fill)
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }

            name?.visibility = View.VISIBLE
            name?.text = categoryTitle
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
        }
    }

    private fun getBundleData() {
        arguments?.let { args ->
            categoryId = args.getString(ARG_CATEGORY_ID) ?: ""
            categoryTitle = args.getString(ARG_CATEGORY_TITLE) ?: ""
//            binding.txtCategory.text = categoryTitle
        }
    }

    private fun initList() {
        binding.apply {
            progressBar.visibility = View.VISIBLE

            viewModel.loadItems(categoryId).observe(viewLifecycleOwner, Observer { items ->
                listView.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                listView.adapter = ItemsListCategoryAdapter(items)
                progressBar.visibility = View.GONE
            })

//            btnBack.setOnClickListener {
//                parentFragmentManager.popBackStack()
//            }
        }
    }
}
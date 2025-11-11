package com.example.project_mobileapplicacion.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.viewModel.MainViewModel
import com.example.project_mobileapplicacion.adapter.CategoryAdapter
import com.example.project_mobileapplicacion.adapter.PopularAdapter
import com.example.project_mobileapplicacion.databinding.FragmentMenuBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel = MainViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureTopBar()
        initBanner()
        initCategory()
        initPopular()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): MenuFragment {
            return MenuFragment()
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
            name?.text = getString(R.string.Main_Menu)
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.loadBanner().observe(viewLifecycleOwner) {
            context?.let { ctx ->
                Glide.with(ctx)
                    .load(it[0].url)
                    .into(binding.banner)
                binding.progressBarBanner.visibility = View.GONE
            }
        }
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.loadCategory().observe(viewLifecycleOwner) {
            binding.recyclerViewCat.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewCat.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        }
    }

    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.loadPopular().observe(viewLifecycleOwner) {
            binding.recyclerViewPopular.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.recyclerViewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        }
    }
}
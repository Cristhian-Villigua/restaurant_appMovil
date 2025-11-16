package com.example.project_mobileapplicacion.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.databinding.FragmentDetailBinding
import com.example.project_mobileapplicacion.helper.CartManager
import com.example.project_mobileapplicacion.model.ItemsModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var item: ItemsModel
    private lateinit var managementCart: CartManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        managementCart = CartManager(requireContext())
        item = arguments?.getSerializable("object") as? ItemsModel
            ?: throw IllegalArgumentException("Item is required")
        configureTopBar()
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(item: ItemsModel): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = Bundle().apply {
                putSerializable("object", item)
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
                setImageResource(R.drawable.ic_favorite)
                visibility = View.VISIBLE
                isClickable = false
                isFocusable = false
            }

            name?.visibility = View.VISIBLE
            name?.text = getString(R.string.Detail)
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
        }
    }

    private fun setupView() {
        binding.apply {
            context?.let { ctx ->
                Glide.with(ctx)
                    .load(item.picUrl[0])
                    .into(picMain)
            }

            textTitle.text = item.title
            txtDescription.text = item.description
            txtPrice.text = "$${item.price}"
            txtRating.text = item.rating.toString()

            itemNumber.text = item.numberInCart.toString()

            btnAddToCart.setOnClickListener {
                item.numberInCart = itemNumber.text.toString().toIntOrNull() ?: 0
                if (item.numberInCart > 0) {
                    managementCart.insertItems(item)
                } else {
                    Toast.makeText(context, "Debe de tener al menos 1 cantidad para ordenar", Toast.LENGTH_SHORT).show()
                }
            }

            btnPlus.setOnClickListener {
                item.numberInCart++
                itemNumber.text = item.numberInCart.toString()
            }

            btnMinus.setOnClickListener {
                if (item.numberInCart > 0) {
                    item.numberInCart--
                    itemNumber.text = item.numberInCart.toString()
                }
            }
        }
    }
}
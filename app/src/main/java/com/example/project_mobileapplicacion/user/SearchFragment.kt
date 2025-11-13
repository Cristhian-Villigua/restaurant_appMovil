package com.example.project_mobileapplicacion.user

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.adapter.ItemsListCategoryAdapter
import com.example.project_mobileapplicacion.databinding.FragmentSearchBinding
import com.example.project_mobileapplicacion.model.ItemsModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ItemsListCategoryAdapter
    private val allItems = mutableListOf<ItemsModel>()
    private val searchResults = mutableListOf<ItemsModel>()
    private var etSearch: TextInputEditText? = null
    private var searchTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureTopBar()
        setupRecyclerView()
        loadAllItems()
    }

    private fun configureTopBar() {
        val act = activity ?: return

        val tilSearch = act.findViewById<TextInputLayout?>(R.id.tilSearch)
        etSearch = act.findViewById(R.id.etSearch)
        val btnLeft = act.findViewById<ImageView?>(R.id.btnLeftBarTop)
        val btnRight = act.findViewById<ImageView?>(R.id.btnRightBarTop)
        val name = act.findViewById<TextView?>(R.id.nameBarTop)

        if (tilSearch == null || etSearch == null) return

        btnLeft?.visibility = View.GONE
        btnRight?.visibility = View.GONE
        name?.visibility = View.GONE

        tilSearch.apply {
            isEnabled = true
            visibility = View.VISIBLE

            val white = ContextCompat.getColor(context, R.color.white)
            val states = arrayOf(
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled)
            )
            val colors = intArrayOf(white, white, white)
            val whiteStates = ColorStateList(states, colors)

            defaultHintTextColor = whiteStates
            setStartIconTintList(whiteStates)
            setBoxStrokeColorStateList(whiteStates)
        }

        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (_binding != null) {
                    performSearch(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etSearch?.apply {
            isEnabled = true
            visibility = View.VISIBLE
            setText("")
            addTextChangedListener(searchTextWatcher)
        }
    }

    private fun setupRecyclerView() {
        adapter = ItemsListCategoryAdapter(searchResults)
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }
    }

    private fun loadAllItems() {
        if (_binding == null) return

        showLoading()
        val database = FirebaseDatabase.getInstance().getReference("Items")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allItems.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(ItemsModel::class.java)
                    item?.let { allItems.add(it) }
                }
                if (_binding != null) {
                    hideLoading()
                    showInitialState()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (_binding != null) {
                    hideLoading()
                    showNoResults()
                }
            }
        })
    }

    private fun performSearch(query: String) {
        if (_binding == null) return

        if (query.isEmpty()) {
            showInitialState()
            return
        }

        showLoading()
        binding.root.postDelayed({
            if (_binding == null) return@postDelayed

            val filteredItems = allItems.filter { item ->
                item.title.contains(query, ignoreCase = true)
                // Aqui se puede agregar más campos para buscar
//                        || item.description.contains(query, ignoreCase = true)
//                        || item.extra.contains(query, ignoreCase = true)
            }

            hideLoading()
            if (filteredItems.isEmpty()) {
                showNoResults()
            } else {
                showResults(filteredItems)
            }
        }, 200)
    }

    private fun showLoading() {
        if (_binding == null) return
        binding.progressBar.visibility = View.VISIBLE
        binding.rvSearchResults.visibility = View.GONE
        binding.layoutNoResults.visibility = View.GONE
        binding.layoutInitialState.visibility = View.GONE
    }

    private fun hideLoading() {
        if (_binding == null) return
        binding.progressBar.visibility = View.GONE
    }

    private fun showInitialState() {
        if (_binding == null) return
        binding.apply {
            rvSearchResults.visibility = View.GONE
            layoutNoResults.visibility = View.GONE
            layoutInitialState.visibility = View.VISIBLE
        }
        searchResults.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showNoResults() {
        if (_binding == null) return
        binding.apply {
            rvSearchResults.visibility = View.GONE
            layoutNoResults.visibility = View.VISIBLE
            layoutInitialState.visibility = View.GONE
        }
        searchResults.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showResults(items: List<ItemsModel>) {
        if (_binding == null) return
        binding.apply {
            rvSearchResults.visibility = View.VISIBLE
            layoutNoResults.visibility = View.GONE
            layoutInitialState.visibility = View.GONE
        }
        searchResults.clear()
        searchResults.addAll(items)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        searchTextWatcher?.let { watcher ->
            etSearch?.removeTextChangedListener(watcher)
        }

        etSearch = null
        searchTextWatcher = null

        _binding = null
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
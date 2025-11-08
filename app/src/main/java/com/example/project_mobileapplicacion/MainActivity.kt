package com.example.project_mobileapplicacion

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_mobileapplicacion.viewModel.MainViewModel
import com.example.project_mobileapplicacion.adapter.CategoryAdapter
import com.example.project_mobileapplicacion.adapter.PopularAdapter
import com.example.project_mobileapplicacion.admin.ProfileActivity
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.databinding.ActivityMainBinding
import com.example.project_mobileapplicacion.user.CartActivity


class MainActivity : AppCompatActivity() {
    private lateinit var imgUser: ImageView
    lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imgUser = findViewById(R.id.imgUser)

        loadUserImage()
        initBanner()
        initCategory()
        initPopular()
        initBottomMenu()
    }

    private fun initBottomMenu() {
        binding.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
    private fun loadUserImage() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (userEmail != null) {
            FirebaseService.getByEmail(userEmail) { fetchedUser ->
                if (fetchedUser != null && fetchedUser.photoBase64 != null) {
                    val photoBase64 = fetchedUser.photoBase64
                    val bitmap = decodeFromBase64(photoBase64)
                    imgUser.setImageBitmap(bitmap)
                } else {
                    imgUser.setImageResource(R.drawable.ic_user)
                }
            }
        } else {
            imgUser.setImageResource(R.drawable.ic_user)
        }
    }

    private fun decodeFromBase64(base64: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.loadBanner().observeForever {
            Glide.with(this@MainActivity)
                .load(it[0].url)
                .into(binding.banner)
            binding.progressBarBanner.visibility = View.GONE
        }
    }

    private fun initCategory(){
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.loadCategory().observeForever {
            binding.recyclerViewCat.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewCat.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        }
        viewModel.loadCategory()
    }

    private fun initPopular(){
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.loadPopular().observeForever {
            binding.recyclerViewPopular.layoutManager = GridLayoutManager(this, 2)
            binding.recyclerViewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        }
        viewModel.loadPopular()
    }
    fun onProfileClick(view: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}

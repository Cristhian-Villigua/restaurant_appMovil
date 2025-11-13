package com.example.project_mobileapplicacion

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.project_mobileapplicacion.admin.IndexFragment
import com.example.project_mobileapplicacion.admin.ProfileFragment
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.menu.MenuFragment
import com.example.project_mobileapplicacion.user.CartFragment
import com.example.project_mobileapplicacion.user.HistoryFragment
import com.example.project_mobileapplicacion.user.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var imgUserProfile: ImageView
    private lateinit var textUserProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textUserProfile = findViewById(R.id.text_user_profile)
        imgUserProfile = findViewById(R.id.img_user_profile)
        imgUserProfile.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            loadUserImage(this)
            setOnClickListener { view ->
                onProfileClick(view)
            }
        }

        // Cargar el fragmento inicial según el rol guardado
        loadDefaultFragment()
    }

    private fun loadDefaultFragment() {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userRole = sharedPreferences.getString("userRole", "Usuario")

        when (userRole) {
            "Administrador" -> {
                configureBottomBarAdmin()
                onAdminClick(View(this))
            }
            "Cocinero" -> {
//                supportFragmentManager.commit {
//                    replace(R.id.fragmentContainer, KitchenFragment.newInstance())
//                }
            }
            "Mesero" -> {
//                supportFragmentManager.commit {
//                    replace(R.id.fragmentContainer, OrdersFragment.newInstance())
//                }
            }
            else -> { // Usuario o Invitado
                configureBottomBarUser()
                onMenuClick(View(this))
            }
        }
    }

    fun onMenuClick(view: View) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, MenuFragment.newInstance())
        }
    }

    fun onCartClick(view: View){
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, CartFragment.newInstance())
        }
    }

    fun onSearchClick(view: View) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, SearchFragment.newInstance())
        }
    }

    fun onHistoryClick(view: View) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, HistoryFragment.newInstance())
        }
    }

    fun onProfileClick(view: View) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, ProfileFragment.newInstance())
        }
    }

    fun onAdminClick(view: View) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, IndexFragment.newInstance())
        }
    }

    private fun configureBottomBarAdmin() {
        val itemHome = findViewById<View>(R.id.item_home)
        val itemCart = findViewById<View>(R.id.item_cart)
        val itemSearch = findViewById<View>(R.id.item_search)
        val itemHistory = findViewById<View>(R.id.item_history)

        itemHome?.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            setOnClickListener { onAdminClick(this) }
        }

        itemCart?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { onCartClick(this) }
        }

        itemSearch?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { onSearchClick(this) }
        }

        itemHistory?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { onHistoryClick(this) }
        }
    }

    private fun configureBottomBarUser() {
        val itemHome = findViewById<View>(R.id.item_home)
        val itemCart = findViewById<View>(R.id.item_cart)
        val itemSearch = findViewById<View>(R.id.item_search)
        val itemHistory = findViewById<View>(R.id.item_history)

        itemHome?.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            setOnClickListener { onMenuClick(this) }
        }

        itemCart?.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            setOnClickListener { onCartClick(this) }
        }

        itemSearch?.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            setOnClickListener { onSearchClick(this) }
        }

        itemHistory?.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            setOnClickListener { onHistoryClick(this) }
        }
    }

    private fun loadUserImage(imageView: ImageView) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)
        if (userEmail != null) {
            FirebaseService.getByEmail(userEmail) { fetchedUser ->
                if (fetchedUser != null && fetchedUser.photoBase64 != null) {
                    try {
                        val photoBase64 = fetchedUser.photoBase64
                        val bytes = Base64.decode(photoBase64, Base64.DEFAULT)
                        textUserProfile.text = "Tú"
                        Glide.with(this)
                            .asBitmap()
                            .load(bytes)
                            .circleCrop()
                            .placeholder(R.drawable.ic_user)
                            .error(R.drawable.ic_user)
                            .into(imageView)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        imageView.setImageResource(R.drawable.ic_user)
                    }
                } else {
                    imageView.setImageResource(R.drawable.ic_user)
                }
            }
        } else {
            imageView.setImageResource(R.drawable.ic_user)
        }
    }
}
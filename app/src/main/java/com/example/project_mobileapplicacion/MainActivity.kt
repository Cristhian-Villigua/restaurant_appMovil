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
import com.example.project_mobileapplicacion.menu.KitchenFragment
import com.example.project_mobileapplicacion.menu.MenuFragment
import com.example.project_mobileapplicacion.user.CartFragment
import com.example.project_mobileapplicacion.user.HistoryFragment
import com.example.project_mobileapplicacion.user.SearchFragment
import com.example.project_mobileapplicacion.waiter.ScanQrFragment

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
                configureBottomBarKitchen()
                onKitchenClick(View(this))
            }
            "Mesero" -> {
                configureBottomBarWaiter()
                onWaiterClick(View(this))
            }
            else -> {
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

    fun onKitchenClick(view: View) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, KitchenFragment.newInstance())
        }
    }

    fun onWaiterClick(view: View) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, ScanQrFragment.newInstance())
        }
    }

    private fun configureBottomBarAdmin() {
        val itemHome = findViewById<View>(R.id.item_home)
        val imgHome = findViewById<ImageView>(R.id.img_home)
        val textHome = findViewById<TextView>(R.id.text_home)
        val itemCart = findViewById<View>(R.id.item_cart)
        val itemSearch = findViewById<View>(R.id.item_search)
        val itemHistory = findViewById<View>(R.id.item_history)

        itemHome?.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            setOnClickListener { onAdminClick(this) }
        }
        imgHome.setImageResource(R.drawable.ic_form_user)
        textHome.text = getString(R.string.Admin)

        itemCart?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { null }
        }

        itemSearch?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { null }
        }

        itemHistory?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { null }
        }
    }

    private fun configureBottomBarKitchen() {
        val itemHome = findViewById<View>(R.id.item_home)
        val imgHome = findViewById<ImageView>(R.id.img_home)
        val textHome = findViewById<TextView>(R.id.text_home)
        val itemCart = findViewById<View>(R.id.item_cart)
        val itemSearch = findViewById<View>(R.id.item_search)
        val itemHistory = findViewById<View>(R.id.item_history)

        itemHome?.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            setOnClickListener { onKitchenClick(this) }
        }
        imgHome.setImageResource(R.drawable.ic_cart)
        textHome.text = getString(R.string.kitchen)

        itemCart?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { null }
        }

        itemSearch?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { null }
        }

        itemHistory?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { null }
        }
    }

    private fun configureBottomBarWaiter() {
        val itemHome = findViewById<View>(R.id.item_home)
        val imgHome = findViewById<ImageView>(R.id.img_home)
        val textHome = findViewById<TextView>(R.id.text_home)
        val itemCart = findViewById<View>(R.id.item_cart)
        val itemSearch = findViewById<View>(R.id.item_search)
        val itemHistory = findViewById<View>(R.id.item_history)

        itemHome?.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            setOnClickListener { onWaiterClick(this) }
        }
        imgHome.setImageResource(R.drawable.ic_clipboard)
        textHome.text = getString(R.string.waiter)

        itemCart?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { null }
        }

        itemSearch?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { null }
        }

        itemHistory?.apply {
            visibility = View.GONE
            isClickable = true
            isFocusable = true
            setOnClickListener { null }
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

    fun refreshUserImage() {
        loadUserImage(imgUserProfile)
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
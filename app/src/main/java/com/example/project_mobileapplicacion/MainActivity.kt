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
import com.example.project_mobileapplicacion.admin.ProfileFragment
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.menu.MenuFragment
import com.example.project_mobileapplicacion.user.CartFragment

class MainActivity : AppCompatActivity() {
    private lateinit var imgUserProfile: ImageView
    private lateinit var textUserProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureTopBottom()

        // Cargar el fragmento por defecto
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, MenuFragment.newInstance())
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
            addToBackStack(null)
        }
    }

    fun onProfileClick(view: View) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, ProfileFragment.newInstance())
            addToBackStack(null)
        }
    }

    private fun configureTopBottom() {
        imgUserProfile = findViewById(R.id.img_user_profile)
        textUserProfile = findViewById(R.id.text_user_profile)

        imgUserProfile.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            loadUserImage(this)
            setOnClickListener { view ->
                onProfileClick(view)
            }
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
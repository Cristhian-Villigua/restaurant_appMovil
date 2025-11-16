package com.example.project_mobileapplicacion.admin

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.auth.LoginActivity
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.model.UserEntity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProfileFragment : Fragment() {

    private var user: UserEntity? = null
    private lateinit var editEtName: TextInputEditText
    private lateinit var editEtLastname: TextInputEditText
    private lateinit var editEtBirthday: TextInputEditText
    private lateinit var editEtPhone: TextInputEditText
    private lateinit var editEtEmail: TextInputEditText
    private lateinit var btnEdit: Button
    private lateinit var imgUser: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollProfile: ScrollView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        editEtName = view.findViewById(R.id.editEtName)
        editEtLastname = view.findViewById(R.id.editEtLastname)
        editEtBirthday = view.findViewById(R.id.editEtBirthday)
        editEtPhone = view.findViewById(R.id.editEtPhone)
        editEtEmail = view.findViewById(R.id.editEtEmail)
        btnEdit = view.findViewById(R.id.btnEdit)
        imgUser = view.findViewById(R.id.imgUser)
        progressBar = view.findViewById(R.id.progressBarProfile)
        scrollProfile = view.findViewById(R.id.scrollProfile)

        btnEdit.setOnClickListener {
            if (user != null) {
                parentFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    replace(R.id.fragmentContainer, UpdateProfileFragment.newInstance())
                    addToBackStack(null)
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, espera mientras se carga tu perfil.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureTopBar()
        loadProfileData()
    }

    private fun configureTopBar() {
        activity?.let { act ->
            val btnLeft = act.findViewById<ImageView>(R.id.btnLeftBarTop)
            val logout = act.findViewById<ImageView>(R.id.btnRightBarTop)
            val name = act.findViewById<TextView>(R.id.nameBarTop)
            val tilSearch = act.findViewById<TextInputLayout>(R.id.tilSearch)
            val etSearch = act.findViewById<TextInputEditText>(R.id.etSearch)

            btnLeft?.apply {
                setImageResource(R.drawable.ic_left_arrow)
                visibility = View.INVISIBLE
                isClickable = false
                isFocusable = false
            }

            logout?.apply {
                setImageResource(R.drawable.ic_logout)
                visibility = View.VISIBLE
                isClickable = true
                isFocusable = true
                setOnClickListener { logoutSession() }
            }

            name?.visibility = View.VISIBLE
            name?.text = getString(R.string.Profile)
            tilSearch?.visibility = View.GONE
            etSearch?.visibility = View.GONE
        }
    }

    private fun loadProfileData() {
        progressBar.visibility = View.VISIBLE
        scrollProfile.visibility = View.INVISIBLE
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)
        if (userEmail != null) {
            FirebaseService.getByEmail(userEmail) { fetchedUser ->
                progressBar.visibility = View.GONE
                scrollProfile.visibility = View.VISIBLE
                if (fetchedUser != null) {
                    user = fetchedUser
                    loadUserData(fetchedUser)
                    btnEdit.isEnabled = true
                } else {
                    Toast.makeText(requireContext(), "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                    setAsGuest()
                }
            }
        } else {
            setAsGuest()
        }
    }

    private fun loadUserData(fetchedUser: UserEntity) {
        editEtName.setText(fetchedUser.name)
        editEtLastname.setText(fetchedUser.lastname)
        editEtBirthday.setText(fetchedUser.birthday)
        editEtPhone.setText(fetchedUser.phone)
        editEtEmail.setText(fetchedUser.email)
        fetchedUser.photoBase64?.let { photoBase64 ->
            val bitmap = decodeFromBase64(photoBase64)
            imgUser.setImageBitmap(bitmap)
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

    private fun setAsGuest() {
        progressBar.visibility = View.GONE
        scrollProfile.visibility = View.VISIBLE
        editEtName.setText(getString(R.string.Login_Guest))
        editEtLastname.setText("N/A")
        editEtBirthday.setText("N/A")
        editEtPhone.setText("N/A")
        editEtEmail.setText("N/A")
        imgUser.setImageResource(R.drawable.img)
        btnEdit.visibility = Button.GONE
    }

    private fun logoutSession() {
        val context = requireContext()
        val sessionPrefs = context.getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
        sessionPrefs.edit().clear().apply()
        val userPrefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userPrefs.edit().clear().apply()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
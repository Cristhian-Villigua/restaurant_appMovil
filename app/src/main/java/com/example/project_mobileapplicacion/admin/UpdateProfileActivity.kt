package com.example.project_mobileapplicacion.admin

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.auth.ValidationActivity
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.database.AppDataBase
import com.example.project_mobileapplicacion.model.UserEntity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.Locale

class UpdateProfileActivity : AppCompatActivity() {
    private var user: UserEntity? = null
    private lateinit var btnBack : LinearLayout
    private lateinit var editTilName: TextInputLayout
    private lateinit var editEtName: TextInputEditText
    private lateinit var editTilLastname: TextInputLayout
    private lateinit var editEtLastname: TextInputEditText
    private lateinit var editTilBirthday: TextInputLayout
    private lateinit var editEtBirthday: TextInputEditText
    private lateinit var editTilPhone: TextInputLayout
    private lateinit var editEtPhone: TextInputEditText
    private lateinit var editTilEmail: TextInputLayout
    private lateinit var editEtEmail: TextInputEditText
    private lateinit var btnUpdate: Button
    private lateinit var imgUser: ImageView
    private lateinit var btnAddPhoto: Button
    private var imageBase64: String? = null
    private val REQUEST_CAMERA = 200
    private val REQUEST_GALLERY = 201

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            imageBase64 = encodeToBase64(it)
            imgUser.setImageBitmap(it)
        }
    }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            imageBase64 = encodeToBase64(bitmap)
            imgUser.setImageBitmap(bitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        editTilName = findViewById(R.id.editTilName)
        editEtName = findViewById(R.id.editEtName)
        editTilLastname = findViewById(R.id.editTilLastname)
        editEtLastname = findViewById(R.id.editEtLastname)
        editTilBirthday = findViewById(R.id.editTilBirthday)
        editEtBirthday = findViewById(R.id.editEtBirthday)
        editTilPhone = findViewById(R.id.editTilPhone)
        editEtPhone = findViewById(R.id.editEtPhone)
        editTilEmail = findViewById(R.id.editTilEmail)
        editEtEmail = findViewById(R.id.editEtEmail)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnBack = findViewById(R.id.btnBack)
        imgUser = findViewById(R.id.imgUser)
        btnAddPhoto = findViewById(R.id.btnAddPhoto)

        btnAddPhoto.setOnClickListener { mostrarOpcionesFoto() }

        enableFields()

        editEtBirthday.setOnClickListener { showDatePickerDialog() }

        btnUpdate.setOnClickListener { saveProfile() }

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (userEmail != null) {
            FirebaseService.getByEmail(userEmail) { fetchedUser ->
                if (fetchedUser != null) {
                    user = fetchedUser
                    loadUserData(fetchedUser)
                    setupRealtimeValidation()
                } else {
                    Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnBack.setOnClickListener {
            startActivity(Intent(this@UpdateProfileActivity, ProfileActivity::class.java))
        }
    }

    private fun enableFields() {
        editEtName.isEnabled = true
        editEtLastname.isEnabled = true
        editEtBirthday.isEnabled = true
        editEtPhone.isEnabled = true
        editEtEmail.isEnabled = false
    }

    private fun loadUserData(user: UserEntity) {
        editEtName.setText(user.name)
        editEtLastname.setText(user.lastname)
        editEtBirthday.setText(user.birthday)
        editEtPhone.setText(user.phone)
        editEtEmail.setText(user.email)
        user.photoBase64?.let {
            val bitmap = decodeFromBase64(it)
            imgUser.setImageBitmap(bitmap)
        }
    }
    private fun decodeFromBase64(base64: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            null
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            R.style.SpinnerDatePicker,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.Companion.format(
                    Locale.US,
                    "%02d/%02d/%04d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )
                editEtBirthday.setText(formattedDate)
                ValidationActivity.validateBirthday(this, editTilBirthday, formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun setupRealtimeValidation() {
        editEtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                ValidationActivity.validateName(this@UpdateProfileActivity, editTilName, s.toString().trim())
            }
        })

        editEtLastname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                ValidationActivity.validateLastname(this@UpdateProfileActivity, editTilLastname, s.toString().trim())
            }
        })

        editEtBirthday.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                ValidationActivity.validateBirthday(this@UpdateProfileActivity, editTilBirthday, s.toString().trim())
            }
        })

        editEtPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                ValidationActivity.validatePhone(this@UpdateProfileActivity, editTilPhone, s.toString().trim())
            }
        })
    }

    private fun saveProfile() {
        val name = editEtName.text.toString().trim()
        val lastname = editEtLastname.text.toString().trim()
        val birthday = editEtBirthday.text.toString().trim()
        val phone = editEtPhone.text.toString().trim()

        val isNameValid = ValidationActivity.validateName(this, editTilName, name)
        val isLastnameValid = ValidationActivity.validateLastname(this, editTilLastname, lastname)
        val isBirthdayValid = ValidationActivity.validateBirthday(this, editTilBirthday, birthday)
        val isPhoneValid = ValidationActivity.validatePhone(this, editTilPhone, phone)

        if (!isNameValid || !isLastnameValid || !isBirthdayValid || !isPhoneValid) {
            Toast.makeText(this, "Por favor corrige los campos marcados", Toast.LENGTH_SHORT).show()
            return
        }

        user?.let { currentUser ->
            val updatedUser = currentUser.copy(
                name = name,
                lastname = lastname,
                birthday = birthday,
                phone = phone,
                photoBase64 = imageBase64?.takeIf { it.isNotEmpty() } ?: currentUser.photoBase64
            )
            lifecycleScope.launch {
            FirebaseService.update(updatedUser)
            val userDao = AppDataBase.Companion.getInstance(applicationContext).userDao()
            userDao.update(updatedUser)
            Toast.makeText(this@UpdateProfileActivity, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
            finish()
            }
        } ?: run {
            Toast.makeText(this, "No se pudo actualizar el perfil", Toast.LENGTH_SHORT).show()
        }
    }
    private fun mostrarOpcionesFoto() {
        val opciones = arrayOf("Tomar foto", "Elegir de galería")
        AlertDialog.Builder(this)
            .setTitle("Selecciona una opción")
            .setItems(opciones) { _, opcion ->
                when (opcion) {
                    0 -> {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            takePicture.launch(null)
                        } else {
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
                        }
                    }
                    1 -> {
                        val galleryPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }

                        if (ActivityCompat.checkSelfPermission(this, galleryPermission) == PackageManager.PERMISSION_GRANTED) {
                            pickImage.launch("image/*")
                        } else {
                            ActivityCompat.requestPermissions(this, arrayOf(galleryPermission), REQUEST_GALLERY)
                        }
                    }
                }
            }
            .setCancelable(true)
            .show()
    }

    private fun encodeToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture.launch(null)
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Se concedió permiso para galería
                pickImage.launch("image/*")
            } else {
                Toast.makeText(this, "Permiso para acceder a la galería denegado", Toast.LENGTH_SHORT).show()
            }
        }
        }
}
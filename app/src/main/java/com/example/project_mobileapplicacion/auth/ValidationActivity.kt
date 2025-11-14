package com.example.project_mobileapplicacion.auth

import android.content.Context
import android.content.res.ColorStateList
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.example.project_mobileapplicacion.R
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object ValidationActivity {

    fun validateName(context: Context, tilName: TextInputLayout, text: String): Boolean {
        val colorError = ContextCompat.getColorStateList(context, R.color.red_primary)!!

        return when {
            text.isEmpty() -> {
                tilName.setBoxStrokeColorStateList(colorError)
                tilName.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilName.helperText = "El nombre es requerido"
                tilName.isCounterEnabled = true
                tilName.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilName.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            text.length < 3 -> {
                tilName.setBoxStrokeColorStateList(colorError)
                tilName.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilName.helperText = "El nombre debe tener al menos 3 caracteres"
                tilName.isCounterEnabled = true
                tilName.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilName.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            !text.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+\$")) -> {
                tilName.setBoxStrokeColorStateList(colorError)
                tilName.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilName.helperText = "El nombre solo puede contener letras"
                tilName.isCounterEnabled = true
                tilName.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilName.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            else -> {
                //tilName.helperText = "Correcto"
                tilName.helperText = null
                tilName.isCounterEnabled = false
                tilName.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_success)))
                val greenColor = ContextCompat.getColorStateList(context, R.color.green_success)!!
                tilName.setBoxStrokeColorStateList(greenColor)
                tilName.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilName.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_check)
                true
            }
        }
    }

    fun validateLastname(context: Context, tilLastname: TextInputLayout, text: String): Boolean {
        val colorError = ContextCompat.getColorStateList(context, R.color.red_primary)!!

        return when {
            text.isEmpty() -> {
                tilLastname.setBoxStrokeColorStateList(colorError)
                tilLastname.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilLastname.helperText = "El apellido es requerido"
                tilLastname.isCounterEnabled = true
                tilLastname.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilLastname.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            text.length < 2 -> {
                tilLastname.setBoxStrokeColorStateList(colorError)
                tilLastname.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilLastname.helperText = "El apellido debe tener al menos 2 caracteres"
                tilLastname.isCounterEnabled = true
                tilLastname.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilLastname.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            !text.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+\$")) -> {
                tilLastname.setBoxStrokeColorStateList(colorError)
                tilLastname.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilLastname.helperText = "El apellido solo puede contener letras"
                tilLastname.isCounterEnabled = true
                tilLastname.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilLastname.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            else -> {
                //tilLastname.helperText = "Correcto"
                tilLastname.helperText = null
                tilLastname.isCounterEnabled = false
                tilLastname.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_success)))
                val greenColor = ContextCompat.getColorStateList(context, R.color.green_success)!!
                tilLastname.setBoxStrokeColorStateList(greenColor)
                tilLastname.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilLastname.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_check)
                true
            }
        }
    }

    fun validateBirthday(context: Context, tilBirthday: TextInputLayout, text: String): Boolean {
        val colorError = ContextCompat.getColorStateList(context, R.color.red_primary)!!

        if (text.isEmpty()) {
            tilBirthday.setBoxStrokeColorStateList(colorError)
            tilBirthday.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
            tilBirthday.helperText = "La fecha de nacimiento es requerida"
            tilBirthday.isCounterEnabled = true
            tilBirthday.endIconMode = TextInputLayout.END_ICON_CUSTOM
            tilBirthday.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
            return false
        }

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return try {
            val date = format.parse(text)
            if (date == null) {
                tilBirthday.setBoxStrokeColorStateList(colorError)
                tilBirthday.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilBirthday.helperText = "Formato de fecha inválido (DD/MM/AAAA)"
                tilBirthday.isCounterEnabled = true
                tilBirthday.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilBirthday.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            } else {
                val selectedCal = Calendar.getInstance()
                selectedCal.time = date

                val now = Calendar.getInstance()
                if (selectedCal.after(now)) {
                    tilBirthday.setBoxStrokeColorStateList(colorError)
                    tilBirthday.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                    tilBirthday.helperText = "La fecha no puede ser futura"
                    tilBirthday.isCounterEnabled = true
                    tilBirthday.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    tilBirthday.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                    return false
                }

                val yearSelected = selectedCal.get(Calendar.YEAR)
                val currentYear = now.get(Calendar.YEAR)
                val age = currentYear - yearSelected

                if (age < 18) {
                    tilBirthday.setBoxStrokeColorStateList(colorError)
                    tilBirthday.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                    tilBirthday.helperText = "Debe ser mayor de 18 años"
                    tilBirthday.isCounterEnabled = true
                    tilBirthday.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    tilBirthday.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                    false
                } else if (age > 50) {
                    tilBirthday.setBoxStrokeColorStateList(colorError)
                    tilBirthday.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                    tilBirthday.helperText = "La fecha debe ser como máximo 50 años atrás"
                    tilBirthday.isCounterEnabled = true
                    tilBirthday.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    tilBirthday.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                    false
                } else {
                    //tilBirthday.helperText = "Correcto"
                    tilBirthday.helperText = null
                    tilBirthday.isCounterEnabled = false
                    tilBirthday.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_success)))
                    val greenColor = ContextCompat.getColorStateList(context, R.color.green_success)!!
                    tilBirthday.setBoxStrokeColorStateList(greenColor)
                    tilBirthday.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    tilBirthday.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_check)
                    true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tilBirthday.setBoxStrokeColorStateList(colorError)
            tilBirthday.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
            tilBirthday.helperText = "Formato de fecha inválido (DD/MM/AAAA)"
            tilBirthday.isCounterEnabled = true
            tilBirthday.endIconMode = TextInputLayout.END_ICON_CUSTOM
            tilBirthday.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
            false
        }
    }

    fun validatePhone(context: Context, tilPhone: TextInputLayout, text: String): Boolean {
        val colorError = ContextCompat.getColorStateList(context, R.color.red_primary)!!

        return when {
            text.isEmpty() -> {
                tilPhone.setBoxStrokeColorStateList(colorError)
                tilPhone.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilPhone.helperText = "El teléfono es requerido"
                tilPhone.isCounterEnabled = true
                tilPhone.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilPhone.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            text.length != 10 || !text.matches(Regex("^\\d{10}\$")) -> {
                tilPhone.setBoxStrokeColorStateList(colorError)
                tilPhone.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilPhone.helperText = "El teléfono debe tener 10 dígitos"
                tilPhone.isCounterEnabled = true
                tilPhone.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilPhone.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            else -> {
                //tilPhone.helperText = "Correcto"
                tilPhone.helperText = null
                tilPhone.isCounterEnabled = false
                tilPhone.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_success)))
                val greenColor = ContextCompat.getColorStateList(context, R.color.green_success)!!
                tilPhone.setBoxStrokeColorStateList(greenColor)
                tilPhone.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilPhone.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_check)
                true
            }
        }
    }

    fun validateEmail(context: Context, tilEmail: TextInputLayout, text: String): Boolean {
        val colorError = ContextCompat.getColorStateList(context, R.color.red_primary)!!
        if (tilEmail.helperText == "Este correo ya está registrado") {
            return false
        }

        return when {
            text.isEmpty() -> {
                tilEmail.setBoxStrokeColorStateList(colorError)
                tilEmail.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilEmail.helperText = "El correo es requerido"
                tilEmail.isCounterEnabled = true
                tilEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilEmail.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            text.length > 30 -> {
                tilEmail.setBoxStrokeColorStateList(colorError)
                tilEmail.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilEmail.helperText = "El correo debe tener máximo 30 caracteres"
                tilEmail.isCounterEnabled = true
                tilEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilEmail.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(text).matches() -> {
                tilEmail.setBoxStrokeColorStateList(colorError)
                tilEmail.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilEmail.helperText = "Ingrese un correo válido"
                tilEmail.isCounterEnabled = true
                tilEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilEmail.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_warning)
                false
            }
            else -> {
                //tilEmail.helperText = "Correcto"
                tilEmail.helperText = null
                tilEmail.isCounterEnabled = false
                tilEmail.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_success)))
                val greenColor = ContextCompat.getColorStateList(context, R.color.green_success)!!
                tilEmail.setBoxStrokeColorStateList(greenColor)
                tilEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilEmail.endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_form_check)
                true
            }
        }
    }

    fun validatePassword(context: Context, tilPassword: TextInputLayout, text: String): Boolean {
        val colorError = ContextCompat.getColorStateList(context, R.color.red_primary)!!

        return when {
            text.isEmpty() -> {
                tilPassword.setBoxStrokeColorStateList(colorError)
                tilPassword.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilPassword.helperText = "La contraseña es requerida"
                tilPassword.isCounterEnabled = true
                false
            }
            text.length < 8 -> {
                tilPassword.setBoxStrokeColorStateList(colorError)
                tilPassword.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilPassword.helperText = "La contraseña debe tener al menos 8 caracteres"
                tilPassword.isCounterEnabled = true
                false
            }
            text.length > 20 -> {
                tilPassword.setBoxStrokeColorStateList(colorError)
                tilPassword.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_primary)))
                tilPassword.helperText = "La contraseña no puede superar 20 caracteres"
                tilPassword.isCounterEnabled = true
                false
            }
            else -> {
                //tilPassword.helperText = "Correcto"
                tilPassword.helperText = null
                tilPassword.isCounterEnabled = false
                tilPassword.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_success)))
                val greenColor = ContextCompat.getColorStateList(context, R.color.green_success)!!
                tilPassword.setBoxStrokeColorStateList(greenColor)
                true
            }
        }
    }
}
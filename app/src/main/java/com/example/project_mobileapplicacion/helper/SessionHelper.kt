package com.example.project_mobileapplicacion.helper

import android.content.Context

object SessionHelper {
    private const val PREFS_NAME = "SessionPrefs"
    private const val USER_ID_KEY = "CURRENT_USER_DOC_ID"

    fun getCurrentUserId(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(USER_ID_KEY, null)
    }

    fun isUserLoggedIn(context: Context): Boolean {
        return getCurrentUserId(context) != null
    }
}
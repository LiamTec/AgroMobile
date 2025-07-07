package com.agromobile.data

import android.content.Context
import android.content.SharedPreferences
import com.agromobile.data.models.UserInfo

object SessionManager {
    private const val PREF_NAME = "AgroMobileSession"
    private const val KEY_ID_TOKEN = "id_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_GOOGLE_ID = "google_id"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString(KEY_ID_TOKEN, token).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(KEY_ID_TOKEN, null)
    }

    fun saveUserInfo(user: UserInfo) {
        prefs.edit()
            .putString(KEY_USER_ID, user.id)
            .putString(KEY_USER_NAME, user.name)
            .putString(KEY_USER_EMAIL, user.email)
            .putString(KEY_GOOGLE_ID, user.googleId)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    fun getUserInfo(): UserInfo? {
        val id = prefs.getString(KEY_USER_ID, null)
        val name = prefs.getString(KEY_USER_NAME, null)
        val email = prefs.getString(KEY_USER_EMAIL, null)
        val googleId = prefs.getString(KEY_GOOGLE_ID, null)

        return if (id != null && name != null && email != null && googleId != null) {
            UserInfo(id, name, email, googleId)
        } else null
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && getAuthToken() != null
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
} 
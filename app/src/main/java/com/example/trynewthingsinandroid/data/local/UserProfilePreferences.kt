package com.example.trynewthingsinandroid.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages local persistence for user profile details (Name, Email, and Avatar)
 * using Android SharedPreferences.
 */
class UserProfilePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Retrieves the saved user name, or [DEFAULT_USER_NAME] if not set.
     */
    fun getUserName(): String = prefs.getString(KEY_NAME, DEFAULT_USER_NAME) ?: DEFAULT_USER_NAME

    /**
     * Retrieves the saved user email, or [DEFAULT_USER_EMAIL] if not set.
     */
    fun getUserEmail(): String = prefs.getString(KEY_EMAIL, DEFAULT_USER_EMAIL) ?: DEFAULT_USER_EMAIL

    /**
     * Retrieves the saved avatar identifier, or [DEFAULT_AVATAR_ID] if not set.
     */
    fun getAvatarId(): String = prefs.getString(KEY_AVATAR, DEFAULT_AVATAR_ID) ?: DEFAULT_AVATAR_ID

    /**
     * Persists updated user profile information (Name, Email, and Avatar ID) to local storage.
     *
     * @param name The user's updated name.
     * @param email The user's updated email address.
     * @param avatarId The selected avatar identifier.
     */
    fun saveProfile(name: String, email: String, avatarId: String) {
        prefs.edit().apply {
            putString(KEY_NAME, name)
            putString(KEY_EMAIL, email)
            putString(KEY_AVATAR, avatarId)
            apply()
        }
    }

    companion object {
        private const val PREF_NAME = "samachar_user_profile_prefs"
        private const val KEY_NAME = "user_name"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_AVATAR = "user_avatar"

        const val DEFAULT_USER_NAME = "Samachar Reader"
        const val DEFAULT_USER_EMAIL = "reader@samachar.ai"
        const val DEFAULT_AVATAR_ID = "default"
    }
}

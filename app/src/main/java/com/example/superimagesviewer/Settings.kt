package com.example.superimagesviewer

import android.content.Context

class Settings {
    companion object {
        private const val PREFERENCES_NAME = "main_preferences"
        private const val INSTAGRAM_USER_NAME_KEY = "instagram_user_name"

        fun getUserName(context: Context): String? {
            return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getString(
                INSTAGRAM_USER_NAME_KEY, null)
        }

        fun setUserName(context: Context, value: String) {
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putString(
                INSTAGRAM_USER_NAME_KEY, value).apply()
        }

    }
}
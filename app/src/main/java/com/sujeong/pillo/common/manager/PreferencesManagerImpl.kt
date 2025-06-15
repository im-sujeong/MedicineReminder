package com.sujeong.pillo.common.manager

import android.content.SharedPreferences

class PreferencesManagerImpl(
    private val prefs : SharedPreferences
): PreferencesManager {
    override fun read(key: String, defaultValue: Long): Long {
        return prefs.getLong(key, defaultValue)
    }

    override fun write(key: String, value: Long) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()

        with(prefsEditor) {
            putLong(key, value)
            commit()
        }
    }

    override fun clear(key: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            remove(key)
            apply()
            commit()
        }
    }
}
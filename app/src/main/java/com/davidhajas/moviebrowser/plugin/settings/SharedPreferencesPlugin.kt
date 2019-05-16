package com.davidhajas.moviebrowser.plugin.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

@SuppressLint("CommitPrefEdits")
class SharedPreferencesPlugin(private val context: Context) : SettingsPlugin {

    private val sharedPreferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    private val editor: SharedPreferences.Editor by lazy { sharedPreferences.edit() }

    // region SettingsPlugin

    override fun contains(key: String) = sharedPreferences.contains(key)

    override fun get(key: String, default: Long): Long = sharedPreferences.getLong(key, default)

    override fun set(key: String, value: Long) {
        editor.putLong(key, value).commit()
    }

    override fun remove(key: String) {
        editor.remove(key).commit()
    }

    // endregion
}
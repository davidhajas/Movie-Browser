package com.davidhajas.moviebrowser.plugin.settings

import com.nhaarman.mockitokotlin2.whenever

class SettingsPluginCoach(private val settingsPlugin: SettingsPlugin) {

    fun trainContains(key: String, contains: Boolean) {
        whenever(settingsPlugin.contains(key)).thenReturn(contains)
    }

    fun trainGet(key: String, value: Long, defaultValue: Long) {
        whenever(settingsPlugin.get(key, defaultValue)).thenReturn(value)
    }
}
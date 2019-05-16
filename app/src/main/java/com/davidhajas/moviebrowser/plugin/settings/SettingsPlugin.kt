package com.davidhajas.moviebrowser.plugin.settings

interface SettingsPlugin {
    fun contains(key: String): Boolean
    fun get(key: String, default: Long): Long
    fun set(key: String, value: Long)
    fun remove(key: String)
}
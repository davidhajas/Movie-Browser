package com.davidhajas.moviebrowser.plugin.security

interface HashPlugin {
    fun hash(input: String): String
}
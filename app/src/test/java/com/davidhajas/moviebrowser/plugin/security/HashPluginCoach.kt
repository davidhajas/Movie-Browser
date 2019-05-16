package com.davidhajas.moviebrowser.plugin.security

import com.nhaarman.mockitokotlin2.whenever

class HashPluginCoach(private val hashPlugin: HashPlugin) {

    fun trainHash(input: String, hash: String) {
        whenever(hashPlugin.hash(input)).thenReturn(hash)
    }
}
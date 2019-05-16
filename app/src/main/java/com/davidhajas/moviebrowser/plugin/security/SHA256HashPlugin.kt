package com.davidhajas.moviebrowser.plugin.security

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


class SHA256HashPlugin : HashPlugin {

    // region HashPlugin

    override fun hash(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(input.toByteArray(StandardCharsets.UTF_8))
        val digest = messageDigest.digest()
        return String.format("%064x", BigInteger(1, digest))
    }

    // endregion
}
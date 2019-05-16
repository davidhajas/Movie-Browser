package com.davidhajas.moviebrowser.usecase.usersession

interface UserSessionInteractor {
    fun isLoggedIn(): Boolean
    fun isUserExists(userName: String): Boolean
    @Throws(LoginException::class, Exception::class)
    fun login(userName: String, password: String)
    @Throws(RegisterException::class, Exception::class)
    fun register(userName: String, password: String, passwordAgain: String)
}
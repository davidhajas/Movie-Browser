package com.davidhajas.moviebrowser.usecase.usersession

import com.nhaarman.mockitokotlin2.whenever

class UserSessionInteractorCoach(private val userSessionInteractor: UserSessionInteractor) {

    fun trainIsLoggedIn(isLoggedIn: Boolean) {
        whenever(userSessionInteractor.isLoggedIn()).thenReturn(isLoggedIn)
    }

    fun trainIsUserExists(userName: String, exists: Boolean) {
        whenever(userSessionInteractor.isUserExists(userName)).thenReturn(exists)
    }

    fun trainLogin(userName: String, password: String, exception: Exception) {
        whenever(userSessionInteractor.login(userName, password)).thenThrow(exception)
    }

    fun trainRegister(userName: String, password: String, passwordAgain: String, exception: Exception) {
        whenever(userSessionInteractor.register(userName, password, passwordAgain)).thenThrow(exception)
    }
}
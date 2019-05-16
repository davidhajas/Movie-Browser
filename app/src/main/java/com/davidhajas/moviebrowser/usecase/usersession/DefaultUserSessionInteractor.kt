package com.davidhajas.moviebrowser.usecase.usersession

import com.davidhajas.moviebrowser.datasource.user.UserDataRepository
import com.davidhajas.moviebrowser.datasource.user.entity.User
import com.davidhajas.moviebrowser.plugin.security.HashPlugin
import com.davidhajas.moviebrowser.plugin.settings.SettingsPlugin
import com.davidhajas.moviebrowser.util.DEFAULT_LOGGED_IN_USER
import com.davidhajas.moviebrowser.util.KEY_LOGGED_IN_USER

class DefaultUserSessionInteractor(
    private val userDataRepository: UserDataRepository,
    private val hashPlugin: HashPlugin,
    private val settingsPlugin: SettingsPlugin
) : UserSessionInteractor {

    // region UserSessionInteractor

    override fun isLoggedIn(): Boolean {
        if (settingsPlugin.contains(KEY_LOGGED_IN_USER)) {
            val userId = settingsPlugin.get(KEY_LOGGED_IN_USER, DEFAULT_LOGGED_IN_USER)
            userDataRepository.getUser(userId)?.let { return true }
        }
        return false
    }

    override fun isUserExists(userName: String): Boolean = userDataRepository.getUser(userName) != null

    override fun login(userName: String, password: String) {
        userDataRepository.getUser(userName)?.let {
            val passwordHash = hashPlugin.hash(password)
            if (passwordHash != it.passwordHash) {
                throw WrongPasswordException()
            }
            settingsPlugin.set(KEY_LOGGED_IN_USER, it.id)
        } ?: throw UserDoesNotExistException()
    }

    override fun register(userName: String, password: String, passwordAgain: String) {
        if (userName.isEmpty()) {
            throw UserNameEmptyException()
        }

        userDataRepository.getUser(userName)?.let {
            throw UserExistsException()
        }

        if (password.isEmpty()) {
            throw PasswordEmptyException()
        }

        if (password.length < 8) {
            throw PasswordTooShortException()
        }

        if (password != passwordAgain) {
            throw PasswordsDifferException()
        }

        val passwordHash = hashPlugin.hash(password)
        val user = User(userName, passwordHash)
        userDataRepository.insertUser(user).also {
            settingsPlugin.set(KEY_LOGGED_IN_USER, it)
        }
    }

    // endregion
}
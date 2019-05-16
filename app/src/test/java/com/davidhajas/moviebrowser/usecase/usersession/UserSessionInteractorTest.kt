package com.davidhajas.moviebrowser.usecase.usersession

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.davidhajas.moviebrowser.datasource.user.UserDataRepository
import com.davidhajas.moviebrowser.datasource.user.UserDataRepositoryCoach
import com.davidhajas.moviebrowser.datasource.user.entity.User
import com.davidhajas.moviebrowser.plugin.security.HashPlugin
import com.davidhajas.moviebrowser.plugin.security.HashPluginCoach
import com.davidhajas.moviebrowser.plugin.settings.SettingsPlugin
import com.davidhajas.moviebrowser.plugin.settings.SettingsPluginCoach
import com.davidhajas.moviebrowser.util.DEFAULT_LOGGED_IN_USER
import com.davidhajas.moviebrowser.util.KEY_LOGGED_IN_USER
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

const val DUMMY_USER_NAME = "Username"
const val DUMMY_PASSWORD = "Password"
const val DUMMY_PASSWORD_WRONG = "PasswordWrong"
const val DUMMY_PASSWORD_HASH = "PasswordHash"
const val DUMMY_PASSWORD_HASH_WRONG = "PasswordHashWrong"
private val DUMMY_USER = User(DUMMY_USER_NAME, DUMMY_PASSWORD_HASH)

class UserSessionInteractorTest {

    private val userDataRepository = mock<UserDataRepository>()
    private val hashPlugin = mock<HashPlugin>()
    private val settingsPlugin = mock<SettingsPlugin>()

    private val userDataRepositoryCoach = UserDataRepositoryCoach(userDataRepository)
    private val hashPluginCoach = HashPluginCoach(hashPlugin)
    private val settingsPluginCoach = SettingsPluginCoach(settingsPlugin)

    private val interactor = DefaultUserSessionInteractor(userDataRepository, hashPlugin, settingsPlugin)

    @After
    fun after() {
        verifyNoMoreInteractions(userDataRepository, hashPlugin, settingsPlugin)
    }

    // region Test cases

    @Test
    fun `isLoggedIn() when settings does not contain logged in user ID`() {
        settingsPluginCoach.trainContains(eq(KEY_LOGGED_IN_USER), false)

        val isLoggedIn = interactor.isLoggedIn()

        verify(settingsPlugin).contains(KEY_LOGGED_IN_USER)
        assertEquals(false, isLoggedIn)
    }

    @Test
    fun `isLoggedIn() when repository does not contain logged in user ID`() {
        settingsPluginCoach.apply {
            trainContains(eq(KEY_LOGGED_IN_USER), true)
            trainGet(eq(KEY_LOGGED_IN_USER), 1L, eq(DEFAULT_LOGGED_IN_USER))
        }
        userDataRepositoryCoach.trainGetUser(1L, null)

        val isLoggedIn = interactor.isLoggedIn()

        verify(settingsPlugin).contains(KEY_LOGGED_IN_USER)
        verify(settingsPlugin).get(KEY_LOGGED_IN_USER, DEFAULT_LOGGED_IN_USER)
        verify(userDataRepository).getUser(1L)
        assertEquals(false, isLoggedIn)
    }

    @Test
    fun `isLoggedIn() when repository contains logged in user ID`() {
        settingsPluginCoach.apply {
            trainContains(eq(KEY_LOGGED_IN_USER), true)
            trainGet(eq(KEY_LOGGED_IN_USER), 1L, eq(DEFAULT_LOGGED_IN_USER))
        }
        userDataRepositoryCoach.trainGetUser(1L, DUMMY_USER)

        val isLoggedIn = interactor.isLoggedIn()

        verify(settingsPlugin).contains(KEY_LOGGED_IN_USER)
        verify(settingsPlugin).get(KEY_LOGGED_IN_USER, DEFAULT_LOGGED_IN_USER)
        verify(userDataRepository).getUser(1L)
        assertEquals(true, isLoggedIn)
    }

    @Test
    fun `isUserExist() when repository returns null`() {
        userDataRepositoryCoach.trainGetUser(anyString(), null)

        val exists = interactor.isUserExists(DUMMY_USER_NAME)

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
        assertEquals(false, exists)
    }

    @Test
    fun `isUserExist() when repository returns user`() {
        userDataRepositoryCoach.trainGetUser(anyString(), DUMMY_USER)

        val exists = interactor.isUserExists(DUMMY_USER_NAME)

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
        assertEquals(true, exists)
    }

    @Test
    fun `login() when user exists and passwords match`() {
        userDataRepositoryCoach.trainGetUser(DUMMY_USER_NAME, DUMMY_USER)
        hashPluginCoach.trainHash(DUMMY_PASSWORD, DUMMY_PASSWORD_HASH)

        interactor.login(DUMMY_USER_NAME, DUMMY_PASSWORD)

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
        verify(hashPlugin).hash(DUMMY_PASSWORD)
        verify(settingsPlugin).set(KEY_LOGGED_IN_USER, 0)
    }

    @Test
    fun `login() when user exists but password is wrong`() {
        userDataRepositoryCoach.trainGetUser(DUMMY_USER_NAME, DUMMY_USER)
        hashPluginCoach.trainHash(DUMMY_PASSWORD, DUMMY_PASSWORD_HASH_WRONG)

        try {
            interactor.login(DUMMY_USER_NAME, DUMMY_PASSWORD)
        } catch (e: WrongPasswordException) {
        }

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
        verify(hashPlugin).hash(DUMMY_PASSWORD)
    }

    @Test
    fun `login() when user does not exist`() {
        userDataRepositoryCoach.trainGetUser(DUMMY_USER_NAME, null)
        hashPluginCoach.trainHash(DUMMY_PASSWORD, DUMMY_PASSWORD_HASH)

        try {
            interactor.login(DUMMY_USER_NAME, DUMMY_PASSWORD)
        } catch (e: UserDoesNotExistException) {
        }

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
    }

    @Test
    fun `register() when username is empty`() {

        try {
            interactor.register("", "", "")
        } catch (e: UserNameEmptyException) {
        }
    }

    @Test
    fun `register() when user exists`() {
        userDataRepositoryCoach.trainGetUser(DUMMY_USER_NAME, DUMMY_USER)

        try {
            interactor.register(DUMMY_USER_NAME, "", "")
        } catch (e: UserExistsException) {
        }

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
    }

    @Test
    fun `register() when password is empty`() {
        userDataRepositoryCoach.trainGetUser(DUMMY_USER_NAME, null)

        try {
            interactor.register(DUMMY_USER_NAME, "", "")
        } catch (e: PasswordEmptyException) {
        }

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
    }

    @Test
    fun `register() when password is too short`() {
        userDataRepositoryCoach.trainGetUser(DUMMY_USER_NAME, null)

        try {
            interactor.register(DUMMY_USER_NAME, "Pass", "")
        } catch (e: PasswordTooShortException) {
        }

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
    }

    @Test
    fun `register() when passwords differ`() {
        userDataRepositoryCoach.trainGetUser(DUMMY_USER_NAME, null)

        try {
            interactor.register(DUMMY_USER_NAME, DUMMY_PASSWORD, DUMMY_PASSWORD_WRONG)
        } catch (e: PasswordsDifferException) {
        }

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
    }

    @Test
    fun `register() when user does not exist and passwords are valid`() {
        userDataRepositoryCoach.trainGetUser(DUMMY_USER_NAME, null)
        hashPluginCoach.trainHash(DUMMY_PASSWORD, DUMMY_PASSWORD_HASH)

        interactor.register(DUMMY_USER_NAME, DUMMY_PASSWORD, DUMMY_PASSWORD)

        verify(userDataRepository).getUser(DUMMY_USER_NAME)
        verify(hashPlugin).hash(DUMMY_PASSWORD)
        verify(userDataRepository).insertUser(User(DUMMY_USER_NAME, DUMMY_PASSWORD_HASH))
        verify(settingsPlugin).set(KEY_LOGGED_IN_USER, 0)
    }

    // endregion
}
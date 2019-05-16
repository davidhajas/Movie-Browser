package com.davidhajas.moviebrowser.module.register

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.module.ContextCoach
import com.davidhajas.moviebrowser.plugin.threading.TestDispatcherProvider
import com.davidhajas.moviebrowser.usecase.usersession.*
import org.junit.After
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

const val DUMMY_USERNAME = "Username"
const val DUMMY_PASSWORD = "Password"
const val DUMMY_PASSWORD_SHORT = "Pass"
const val DUMMY_PASSWORD_DIFFERERNT = "OtherPassword"

class RegisterPresenterTest {

    private val view = mock<RegisterView>()
    private val context = mock<Context>()
    private val lifecycle = mock<Lifecycle>()
    private val dispatcherProvider = TestDispatcherProvider()
    private val userSessionInteractor = mock<UserSessionInteractor>()

    private val contextCoach = ContextCoach(context)
    private val userSessionInteractorCoach = UserSessionInteractorCoach(userSessionInteractor)

    private val presenter = RegisterPresenter(view, context, lifecycle, dispatcherProvider, userSessionInteractor)

    @After
    fun after() {
        verifyNoMoreInteractions(view, context, userSessionInteractor)
    }

    // region Test cases

    @Test
    fun `When the view is ready`() {

        presenter.onViewReady()

        verify(view).hideLoading()
        verify(view).enableForm()
    }

    @Test
    fun `Given username does not exist, when clicking register`() {

        presenter.register(DUMMY_USERNAME, DUMMY_PASSWORD, DUMMY_PASSWORD)

        verifyOnRegisterClickCommon()
        verify(view).proceedToMainScreen()
    }

    @Test
    fun `Given username is empty, when clicking register`() {
        userSessionInteractorCoach.trainRegister(eq(""), anyString(), anyString(), UserNameEmptyException())

        val errorResId = R.string.error_username_empty
        val errorString = "Please specify username."
        contextCoach.trainGetString(errorResId, errorString)

        presenter.register("", DUMMY_PASSWORD, DUMMY_PASSWORD)

        verifyOnRegisterClickCommon()
        verify(context).getString(errorResId)
        verify(view).showError(errorString)
    }

    @Test
    fun `Given username exists, when clicking register`() {
        userSessionInteractorCoach.trainRegister(anyString(), anyString(), anyString(), UserExistsException())

        val errorResId = R.string.error_user_exists
        val errorString = "User already exists."
        contextCoach.trainGetString(errorResId, errorString)

        presenter.register(DUMMY_USERNAME, DUMMY_PASSWORD, DUMMY_PASSWORD)

        verifyOnRegisterClickCommon()
        verify(context).getString(errorResId)
        verify(view).showError(errorString)
    }

    @Test
    fun `Given password is empty, when clicking register`() {
        userSessionInteractorCoach.trainRegister(anyString(), eq(""), anyString(), PasswordEmptyException())

        val errorResId = R.string.error_password_empty
        val errorString = "Please specify password."
        contextCoach.trainGetString(errorResId, errorString)

        presenter.register(DUMMY_USERNAME, "", DUMMY_PASSWORD)

        verifyOnRegisterClickCommon()
        verify(context).getString(errorResId)
        verify(view).showError(errorString)
    }

    @Test
    fun `Given password is too short, when clicking register`() {
        userSessionInteractorCoach.trainRegister(anyString(), anyString(), anyString(), PasswordTooShortException())

        val errorResId = R.string.error_password_short
        val errorString = "Password is too short."
        contextCoach.trainGetString(errorResId, errorString)

        presenter.register(DUMMY_USERNAME, DUMMY_PASSWORD_SHORT, DUMMY_PASSWORD)

        verifyOnRegisterClickCommon()
        verify(context).getString(errorResId)
        verify(view).showError(errorString)
    }

    @Test
    fun `Given password is not the same, when clicking register`() {
        userSessionInteractorCoach.trainRegister(anyString(), anyString(), anyString(), PasswordsDifferException())

        val errorResId = R.string.error_passwords_differ
        val errorString = "Passwords must match."
        contextCoach.trainGetString(errorResId, errorString)

        presenter.register(DUMMY_USERNAME, DUMMY_PASSWORD, DUMMY_PASSWORD_DIFFERERNT)

        verifyOnRegisterClickCommon()
        verify(context).getString(errorResId)
        verify(view).showError(errorString)
    }

    // endregion

    // region Helpers

    private fun verifyOnRegisterClickCommon() {
        verify(view).showLoading()
        verify(view).disableForm()
        verify(userSessionInteractor).register(anyString(), anyString(), anyString())
        verify(view).clearPasswords()
        verify(view).enableForm()
        verify(view).hideLoading()
    }

    // endregion
}
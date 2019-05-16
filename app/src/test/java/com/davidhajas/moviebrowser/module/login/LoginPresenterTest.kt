package com.davidhajas.moviebrowser.module.login

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.module.ContextCoach
import com.davidhajas.moviebrowser.plugin.threading.TestDispatcherProvider
import com.davidhajas.moviebrowser.usecase.usersession.UserDoesNotExistException
import com.davidhajas.moviebrowser.usecase.usersession.UserSessionInteractor
import com.davidhajas.moviebrowser.usecase.usersession.UserSessionInteractorCoach
import com.davidhajas.moviebrowser.usecase.usersession.WrongPasswordException
import org.junit.After
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

const val DUMMY_USERNAME = "Username"
const val DUMMY_PASSWORD = "Password"

class LoginPresenterTest {

    private val view = mock<LoginView>()
    private val context = mock<Context>()
    private val lifecycle = mock<Lifecycle>()
    private val dispatcherProvider = TestDispatcherProvider()
    private val userSessionInteractor = mock<UserSessionInteractor>()

    private val contextCoach = ContextCoach(context)
    private val userSessionInteractorCoach = UserSessionInteractorCoach(userSessionInteractor)

    private val presenter = LoginPresenter(view, context, lifecycle, dispatcherProvider, userSessionInteractor)

    @After
    fun after() {
        verifyNoMoreInteractions(view, context, userSessionInteractor)
    }

    // region Test cases

    @Test
    fun `Given user has not yet logged in, when the view is ready`() {
        userSessionInteractorCoach.trainIsLoggedIn(false)

        presenter.onViewReady()

        verifyOnStartCommon()
        verify(view).enableForm()
    }

    @Test
    fun `Given user already logged in, when the view is ready`() {
        userSessionInteractorCoach.trainIsLoggedIn(true)

        presenter.onViewReady()

        verifyOnStartCommon()
        verify(view).proceedToMainScreen()
    }

    @Test
    fun `Given user does not exist, when clicking login`() {
        userSessionInteractorCoach.apply {
            trainIsUserExists(anyString(), false)
            trainLogin(anyString(), anyString(), UserDoesNotExistException())
        }
        val errorResId = R.string.error_user_does_not_exist
        val errorString = "User does not exist!"
        contextCoach.trainGetString(errorResId, errorString)

        presenter.onLoginClick(DUMMY_USERNAME, DUMMY_PASSWORD)

        verifyOnLoginClickCommon()
        verify(context).getString(errorResId)
        verify(view).showError(errorString)
    }

    @Test
    fun `Given user exists but password is wrong, when clicking login`() {
        userSessionInteractorCoach.apply {
            trainIsUserExists(anyString(), true)
            trainLogin(anyString(), anyString(), WrongPasswordException())
        }
        val errorResId = R.string.error_password_is_wrong
        val errorString = "Username or password is wrong!"
        contextCoach.trainGetString(errorResId, errorString)

        presenter.onLoginClick(DUMMY_USERNAME, DUMMY_PASSWORD)

        verifyOnLoginClickCommon()
        verify(context).getString(errorResId)
        verify(view).showError(errorString)
    }

    @Test
    fun `Given user exists but unknown error occurs, when clicking login`() {
        userSessionInteractorCoach.apply {
            trainIsUserExists(anyString(), true)
            trainLogin(anyString(), anyString(), Exception())
        }
        val errorResId = R.string.error_unknown
        val errorString = "Unknown error."
        contextCoach.trainGetString(errorResId, errorString)

        presenter.onLoginClick(DUMMY_USERNAME, DUMMY_PASSWORD)

        verifyOnLoginClickCommon()
        verify(context).getString(errorResId)
        verify(view).showError(errorString)
    }

    @Test
    fun `Given user exists, when clicking login`() {
        userSessionInteractorCoach.apply {
            trainIsUserExists(anyString(), true)
        }

        presenter.onLoginClick("Username", DUMMY_PASSWORD)

        verifyOnLoginClickCommon()
        verify(view).proceedToMainScreen()
    }

    @Test
    fun `When clicking register`() {

        presenter.onRegisterClick()

        verify(view).clearPassword()
        verify(view).proceedToRegisterScreen()
    }

    // endregion

    // region Helpers

    private fun verifyOnStartCommon() {
        verify(view).disableForm()
        verify(view).showLoading()
        verify(userSessionInteractor).isLoggedIn()
        verify(view).hideLoading()
    }

    private fun verifyOnLoginClickCommon() {
        verify(view).showLoading()
        verify(view).disableForm()
        verify(userSessionInteractor).login(anyString(), anyString())
        verify(view).clearPassword()
        verify(view).enableForm()
        verify(view).hideLoading()
    }

    // endregion
}
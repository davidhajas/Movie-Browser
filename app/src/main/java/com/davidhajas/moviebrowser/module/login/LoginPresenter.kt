package com.davidhajas.moviebrowser.module.login

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.module.BasePresenter
import com.davidhajas.moviebrowser.plugin.threading.DispatcherProvider
import com.davidhajas.moviebrowser.usecase.usersession.UserDoesNotExistException
import com.davidhajas.moviebrowser.usecase.usersession.UserSessionInteractor
import com.davidhajas.moviebrowser.usecase.usersession.WrongPasswordException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPresenter(
    private var view: LoginView,
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val dispatcherProvider: DispatcherProvider,
    private val userSessionInteractor: UserSessionInteractor
) : BasePresenter(view, context, lifecycle, dispatcherProvider) {

    // region BasePresenter

    override fun onViewReady() {
        super.onViewReady()
        mainScope.launch {
            view.disableForm()
            view.showLoading()
            val userExists = withContext(dispatcherProvider.background) {
                userSessionInteractor.isLoggedIn()
            }
            view.hideLoading()
            if (userExists) {
                view.proceedToMainScreen()
            } else {
                view.enableForm()
            }
        }
    }

    // endregion

    // region View events

    fun onLoginClick(userName: String, password: String) {
        mainScope.launch {
            view.disableForm()
            view.showLoading()
            try {
                withContext(dispatcherProvider.background) {
                    userSessionInteractor.login(userName, password)
                }
            } catch (e: UserDoesNotExistException) {
                updateViewAfterLogin()
                view.showError(context.getString(R.string.error_user_does_not_exist))
                return@launch
            } catch (e: WrongPasswordException) {
                updateViewAfterLogin()
                view.showError(context.getString(R.string.error_password_is_wrong))
                return@launch
            } catch (e: Exception) {
                updateViewAfterLogin()
                view.showError(context.getString(R.string.error_unknown))
                return@launch
            }
            updateViewAfterLogin()
            view.proceedToMainScreen()
        }
    }

    fun onRegisterClick() {
        view.clearPassword()
        view.proceedToRegisterScreen()
    }

    // endregion

    // region Helpers

    private fun updateViewAfterLogin() {
        view.clearPassword()
        view.enableForm()
        view.hideLoading()
    }

    // endregion
}
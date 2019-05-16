package com.davidhajas.moviebrowser.module.register

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.module.BasePresenter
import com.davidhajas.moviebrowser.plugin.threading.DispatcherProvider
import com.davidhajas.moviebrowser.usecase.usersession.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterPresenter(
    private val view: RegisterView,
    private val context: Context,
    private val lifeCycle: Lifecycle,
    private val dispatcherProvider: DispatcherProvider,
    private val userSessionInteractor: UserSessionInteractor
) : BasePresenter(view, context, lifeCycle, dispatcherProvider) {

    // region BasePresenter

    override fun onViewReady() {
        super.onViewReady()

        view.hideLoading()
        view.enableForm()
    }

    // endregion

    // region View events

    fun register(userName: String, password: String, passwordAgain: String) {
        mainScope.launch {
            view.disableForm()
            view.showLoading()
            try {
                withContext(dispatcherProvider.background) {
                    userSessionInteractor.register(userName, password, passwordAgain)
                }
            } catch (e: UserNameEmptyException) {
                updateViewAfterRegister()
                view.showError(context.getString(R.string.error_username_empty))
                return@launch
            } catch (e: UserExistsException) {
                updateViewAfterRegister()
                view.showError(context.getString(R.string.error_user_exists))
                return@launch
            } catch (e: PasswordEmptyException) {
                updateViewAfterRegister()
                view.showError(context.getString(R.string.error_password_empty))
                return@launch
            } catch (e: PasswordTooShortException) {
                updateViewAfterRegister()
                view.showError(context.getString(R.string.error_password_short))
                return@launch
            } catch (e: PasswordsDifferException) {
                updateViewAfterRegister()
                view.showError(context.getString(R.string.error_passwords_differ))
                return@launch
            }
            updateViewAfterRegister()
            view.proceedToMainScreen()
        }
    }

    // endregion

    // region Helpers

    private fun updateViewAfterRegister() {
        view.clearPasswords()
        view.enableForm()
        view.hideLoading()
    }

    // endregion
}
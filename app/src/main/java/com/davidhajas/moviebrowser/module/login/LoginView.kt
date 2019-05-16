package com.davidhajas.moviebrowser.module.login

import com.davidhajas.moviebrowser.module.View

interface LoginView : View {
    fun disableForm()
    fun enableForm()
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
    fun proceedToMainScreen()
    fun clearPassword()
    fun proceedToRegisterScreen()
}
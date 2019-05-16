package com.davidhajas.moviebrowser.module.register

import com.davidhajas.moviebrowser.module.View

interface RegisterView : View {
    fun disableForm()
    fun enableForm()
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
    fun proceedToMainScreen()
    fun clearPasswords()
}
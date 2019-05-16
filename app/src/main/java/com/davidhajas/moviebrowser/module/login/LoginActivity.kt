package com.davidhajas.moviebrowser.module.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.davidhajas.moviebrowser.MainActivity
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.module.register.RegisterActivity
import com.davidhajas.moviebrowser.plugin.threading.DefaultDispatcherProvider
import com.davidhajas.moviebrowser.util.Injector
import kotlinx.android.synthetic.main.activity_login.*

const val KEY_USERNAME = "Username"

class LoginActivity : AppCompatActivity(), LoginView {

    private val presenter: LoginPresenter by lazy {
        val userDataRepository = Injector.injectUserDataRepository(this)
        val hashPlugin = Injector.injectHashPlugin()
        val settingsPlugin = Injector.injectSettingsPlugin(this)
        val userSessionInteractor = Injector.injectUserSessionInteractor(userDataRepository, hashPlugin, settingsPlugin)
        LoginPresenter(this, this, lifecycle, DefaultDispatcherProvider(), userSessionInteractor)
    }

    // region Activity callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        savedInstanceState?.let {
            etUsername.setText(it.getString(KEY_USERNAME, ""))
        }

        setSupportActionBar(toolBar)
        supportActionBar?.title = getString(R.string.title_login)

        btnLogin.setOnClickListener { presenter.onLoginClick(etUsername.text.toString(), etPassword.text.toString()) }
        btnRegister.setOnClickListener { presenter.onRegisterClick() }

        presenter.onViewReady()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.apply {
            putString(KEY_USERNAME, etUsername.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    // endregion

    // region LoginView

    override fun disableForm() {
        etUsername.isEnabled = false
        etPassword.isEnabled = false
        btnLogin.isEnabled = false
        btnRegister.isEnabled = false
    }

    override fun enableForm() {
        etUsername.isEnabled = true
        etPassword.isEnabled = true
        btnLogin.isEnabled = true
        btnRegister.isEnabled = true
    }

    override fun showLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun proceedToMainScreen() {
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }.also {
            startActivity(it)
            finish()
        }
    }

    override fun clearPassword() {
        etPassword.text.clear()
    }

    override fun proceedToRegisterScreen() {
        Intent(this, RegisterActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    // endregion
}
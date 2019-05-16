package com.davidhajas.moviebrowser.module.register

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.davidhajas.moviebrowser.MainActivity
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.plugin.threading.DefaultDispatcherProvider
import com.davidhajas.moviebrowser.util.Injector
import kotlinx.android.synthetic.main.activity_register.*

const val KEY_USERNAME = "Username"

class RegisterActivity : AppCompatActivity(), RegisterView {

    private val presenter: RegisterPresenter by lazy {
        val userDataRepository = Injector.injectUserDataRepository(this)
        val hashPlugin = Injector.injectHashPlugin()
        val settingsPlugin = Injector.injectSettingsPlugin(this)
        val userSessionInteractor = Injector.injectUserSessionInteractor(userDataRepository, hashPlugin, settingsPlugin)
        RegisterPresenter(this, this, lifecycle, DefaultDispatcherProvider(), userSessionInteractor)
    }

    // region Activity callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        savedInstanceState?.let {
            etUsername.setText(it.getString(KEY_USERNAME, ""))
        }

        setSupportActionBar(toolBar)
        supportActionBar?.apply {
            title = getString(R.string.title_register)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        btnRegister.setOnClickListener {
            presenter.register(
                etUsername.text.toString(),
                etPassword.text.toString(),
                etPasswordAgain.text.toString()
            )
        }

        presenter.onViewReady()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.apply {
            putString(KEY_USERNAME, etUsername.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // endregion

    // region RegisterView

    override fun disableForm() {
        etUsername.isEnabled = false
        etPassword.isEnabled = false
        etPasswordAgain.isEnabled = false
        btnRegister.isEnabled = false
    }

    override fun enableForm() {
        etUsername.isEnabled = true
        etPassword.isEnabled = true
        etPasswordAgain.isEnabled = true
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

    override fun clearPasswords() {
        etPassword.text.clear()
        etPasswordAgain.text.clear()
    }

    // endregion
}

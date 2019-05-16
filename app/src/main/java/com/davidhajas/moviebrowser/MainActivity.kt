package com.davidhajas.moviebrowser

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.davidhajas.moviebrowser.module.favorites.FavoritesFragment
import com.davidhajas.moviebrowser.module.login.LoginActivity
import com.davidhajas.moviebrowser.module.movielist.MovieListFragment
import com.davidhajas.moviebrowser.util.Injector
import com.davidhajas.moviebrowser.util.KEY_LOGGED_IN_USER
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // region Activity callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolBar)
        supportActionBar?.title = getString(R.string.app_name)

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.main, it)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_logout -> {
                Injector.injectSettingsPlugin(this).remove(KEY_LOGGED_IN_USER)
                Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }.also {
                    startActivity(it)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // endregion

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                setFragment(MovieListFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                setFragment(FavoritesFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}

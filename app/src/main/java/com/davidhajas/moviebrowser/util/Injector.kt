package com.davidhajas.moviebrowser.util

import android.content.Context
import com.davidhajas.moviebrowser.datasource.RoomDatabaseImpl
import com.davidhajas.moviebrowser.datasource.movie.MovieLocalRepository
import com.davidhajas.moviebrowser.datasource.movie.MovieRemoteRepository
import com.davidhajas.moviebrowser.datasource.user.UserDataRepository
import com.davidhajas.moviebrowser.plugin.security.HashPlugin
import com.davidhajas.moviebrowser.plugin.security.SHA256HashPlugin
import com.davidhajas.moviebrowser.plugin.settings.SettingsPlugin
import com.davidhajas.moviebrowser.plugin.settings.SharedPreferencesPlugin
import com.davidhajas.moviebrowser.usecase.movie.DefaultMovieInteractor
import com.davidhajas.moviebrowser.usecase.movie.MovieInteractor
import com.davidhajas.moviebrowser.usecase.usersession.DefaultUserSessionInteractor
import com.davidhajas.moviebrowser.usecase.usersession.UserSessionInteractor

object Injector {

    // region Interactors

    private var userSessionInteractor: UserSessionInteractor? = null
    fun injectUserSessionInteractor(
        userDataRepository: UserDataRepository,
        hashPlugin: HashPlugin,
        settingsPlugin: SettingsPlugin
    ): UserSessionInteractor {
        return userSessionInteractor ?: DefaultUserSessionInteractor(
            userDataRepository,
            hashPlugin,
            settingsPlugin
        ).also {
            userSessionInteractor = it
        }
    }

    private var movieInteractor: MovieInteractor? = null
    fun injectMovieInteractor(
        movieRemoteRepository: MovieRemoteRepository,
        movieLocalRepository: MovieLocalRepository
    ): MovieInteractor {
        return movieInteractor ?: DefaultMovieInteractor(movieRemoteRepository, movieLocalRepository).also {
            movieInteractor = it
        }
    }

    // endregion

    // region Repositories

    private var userDataRepository: UserDataRepository? = null
    fun injectUserDataRepository(context: Context): UserDataRepository {
        return userDataRepository ?: RoomDatabaseImpl.getInstance(context).userDao().also {
            userDataRepository = it
        }
    }

    private var movieLocalRepository: MovieLocalRepository? = null
    fun injectMovieLocalRepository(context: Context): MovieLocalRepository {
        return movieLocalRepository ?: RoomDatabaseImpl.getInstance(context).movieDao().also {
            movieLocalRepository = it
        }
    }

    private var movieRemoteRepository: MovieRemoteRepository? = null
    fun injectMovieRemoteRepository(): MovieRemoteRepository {
        return movieRemoteRepository ?: RetrofitFactory.createRetrofitService().also {
            movieRemoteRepository = it
        }
    }

    // endregion

    // region Plugins

    private var hashPlugin: HashPlugin? = null
    fun injectHashPlugin(): HashPlugin {
        return hashPlugin ?: SHA256HashPlugin().also {
            hashPlugin = it
        }
    }

    private var settingsPlugin: SettingsPlugin? = null
    fun injectSettingsPlugin(context: Context): SettingsPlugin {
        return settingsPlugin ?: SharedPreferencesPlugin(context).also {
            settingsPlugin = it
        }
    }

    // endregion

}
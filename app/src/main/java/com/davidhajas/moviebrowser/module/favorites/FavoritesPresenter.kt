package com.davidhajas.moviebrowser.module.favorites

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.davidhajas.moviebrowser.datasource.movie.entity.Movie
import com.davidhajas.moviebrowser.module.BasePresenter
import com.davidhajas.moviebrowser.plugin.threading.DispatcherProvider
import com.davidhajas.moviebrowser.usecase.movie.MovieInteractor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesPresenter(
    private var view: FavoritesView,
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val dispatcherProvider: DispatcherProvider,
    private val movieInteractor: MovieInteractor
) : BasePresenter(view, context, lifecycle, dispatcherProvider) {

    // region BasePresenter

    override fun onViewReady() {
        super.onViewReady()

        mainScope.launch {
            val list = withContext(dispatcherProvider.background) {
                movieInteractor.getFavorites()
            }
            if (list.isNotEmpty()) {
                view.showList(list)
                view.hideHint()
            } else {
                view.hideList()
                view.showHint()
            }
        }
    }

    // endregion

    // region View events

    fun onFavoriteClicked(movie: Movie) {
        mainScope.launch {
            val list = withContext(dispatcherProvider.background) {
                movieInteractor.toggleFavorite(movie)
                movieInteractor.getFavorites()
            }
            if (list.isNotEmpty()) {
                view.showList(list)
                view.hideHint()
            } else {
                view.hideList()
                view.showHint()
            }
        }
    }

    // endregion
}
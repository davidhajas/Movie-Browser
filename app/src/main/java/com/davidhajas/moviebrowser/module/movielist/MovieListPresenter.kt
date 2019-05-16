package com.davidhajas.moviebrowser.module.movielist

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.datasource.movie.entity.Movie
import com.davidhajas.moviebrowser.module.BasePresenter
import com.davidhajas.moviebrowser.plugin.threading.DispatcherProvider
import com.davidhajas.moviebrowser.usecase.movie.MovieInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MovieListPresenter(
    private var view: MovieListView,
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val dispatcherProvider: DispatcherProvider,
    private val movieInteractor: MovieInteractor
) : BasePresenter(view, context, lifecycle, dispatcherProvider) {

    private var filter = ""

    // region BasePresenter

    override fun onViewReady() {
        super.onViewReady()

        view.showHint()
        view.hideList()
        view.hideLoading()
    }

    // endregion

    // region View events

    fun onSearch(searchString: String) {
        filter = searchString
        if (filter.isEmpty()) {
            view.showHint()
            view.hideList()
            return
        } else {
            view.hideHint()
        }
        mainScope.launch {
            delay(400)
            if(searchString != filter) {
                return@launch
            }
            view.showLoading()
            try {
                val list = withContext(dispatcherProvider.background) {
                    movieInteractor.getMovies(filter)
                }
                if (list.isNotEmpty()) {
                    view.showList(list)
                } else {
                    view.hideList()
                    view.showError(context.getString(R.string.error_empty_list))
                }
                view.hideLoading()
            } catch (e: IOException) {
                view.showError(context.getString(R.string.error_io))
                view.hideLoading()
            }
        }
    }

    fun onRefresh(searchString: String) {
        onSearch(searchString)
    }

    fun onFavoriteClicked(movie: Movie) {
        mainScope.launch {
            val favorite = withContext(dispatcherProvider.background) {
                movieInteractor.toggleFavorite(movie)
            }
            view.updateFavorite(movie, favorite)
        }
    }

    // endregion
}
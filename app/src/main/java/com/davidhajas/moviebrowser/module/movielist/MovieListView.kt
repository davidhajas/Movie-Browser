package com.davidhajas.moviebrowser.module.movielist

import com.davidhajas.moviebrowser.datasource.movie.entity.Movie
import com.davidhajas.moviebrowser.module.View

interface MovieListView : View {
    fun showLoading()
    fun hideLoading()
    fun showList(list: List<Movie>)
    fun updateFavorite(movie: Movie, favorite: Boolean)
    fun hideList()
    fun showHint()
    fun hideHint()
    fun showError(message: String)
}
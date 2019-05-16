package com.davidhajas.moviebrowser.module.favorites

import com.davidhajas.moviebrowser.datasource.movie.entity.Movie
import com.davidhajas.moviebrowser.module.View

interface FavoritesView : View {
    fun showList(list: List<Movie>)
    fun hideList()
    fun showHint()
    fun hideHint()
}
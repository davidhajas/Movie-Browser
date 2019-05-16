package com.davidhajas.moviebrowser.datasource.movie

import com.davidhajas.moviebrowser.datasource.movie.entity.Movie

interface MovieLocalRepository {
    fun getMovies(): List<Movie>
    fun getMovie(id: String): Movie?
    fun insertMovie(movie: Movie): Long
    fun updateFavorite(id: String, favorite: Boolean)
}
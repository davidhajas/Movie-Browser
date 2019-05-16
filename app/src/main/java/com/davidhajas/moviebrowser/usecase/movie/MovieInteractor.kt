package com.davidhajas.moviebrowser.usecase.movie

import com.davidhajas.moviebrowser.datasource.movie.entity.Movie
import java.io.IOException

interface MovieInteractor {
    @Throws(IOException::class)
    suspend fun getMovies(filter: String): List<Movie>
    fun getFavorites(): List<Movie>
    fun toggleFavorite(movie: Movie): Boolean
}
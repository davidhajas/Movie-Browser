package com.davidhajas.moviebrowser.usecase.movie

import com.davidhajas.moviebrowser.datasource.movie.API_KEY
import com.davidhajas.moviebrowser.datasource.movie.MovieLocalRepository
import com.davidhajas.moviebrowser.datasource.movie.MovieRemoteRepository
import com.davidhajas.moviebrowser.datasource.movie.entity.Movie

class DefaultMovieInteractor(
    private val movieRemoteRepository: MovieRemoteRepository,
    private val movieLocalRepository: MovieLocalRepository
) : MovieInteractor {

    // region MovieInteractor

    override suspend fun getMovies(filter: String): List<Movie> {
        val movieResponse = movieRemoteRepository.getMovies(API_KEY, filter).await()
        val list = movieResponse.search
        movieResponse.search?.forEach {
            movieLocalRepository.getMovie(it.id)?.let { localMovie ->
                it.favorite = localMovie.favorite
            }
        }
        return list ?: listOf()
    }

    override fun toggleFavorite(movie: Movie): Boolean {
        movie.favorite = !movie.favorite
        if (movieLocalRepository.getMovie(movie.id) == null) {
            movieLocalRepository.insertMovie(movie)
        }
        movieLocalRepository.updateFavorite(movie.id, movie.favorite)

        return movieLocalRepository.getMovie(movie.id)?.favorite ?: false
    }

    override fun getFavorites(): List<Movie> {
        return movieLocalRepository.getMovies().filter { it.favorite }
    }

    // endregion
}
package com.davidhajas.moviebrowser.datasource.movie

import com.davidhajas.moviebrowser.datasource.movie.entity.MovieResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "be212cd9"

@Suppress("DeferredIsResult")
interface MovieRemoteRepository {

    @GET(".")
    fun getMovies(
        @Query("apikey") apikey: String,
        @Query("s") searchFilter: String
    ): Deferred<MovieResponse>
}
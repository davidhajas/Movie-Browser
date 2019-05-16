package com.davidhajas.moviebrowser.util

import com.davidhajas.moviebrowser.datasource.movie.MovieRemoteRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    const val BASE_URL = "http://www.omdbapi.com/"

    fun createRetrofitService(): MovieRemoteRepository {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(MovieRemoteRepository::class.java)
    }
}
package com.davidhajas.moviebrowser.datasource.movie.entity

import com.google.gson.annotations.SerializedName

data class MovieResponse(@SerializedName("Search") val search: List<Movie>?)
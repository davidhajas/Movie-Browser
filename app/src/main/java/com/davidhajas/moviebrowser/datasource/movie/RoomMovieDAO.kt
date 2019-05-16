package com.davidhajas.moviebrowser.datasource.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.davidhajas.moviebrowser.datasource.movie.entity.Movie

@Dao
interface RoomMovieDAO : MovieLocalRepository {

// region MovieLocalRepository

    @Query("SELECT * from movies")
    override fun getMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    override fun getMovie(id: String): Movie?

    @Insert
    override fun insertMovie(movie: Movie): Long

    @Query("UPDATE movies SET favorite = :favorite WHERE id = :id")
    override fun updateFavorite(id: String, favorite: Boolean)

    // endregion
}
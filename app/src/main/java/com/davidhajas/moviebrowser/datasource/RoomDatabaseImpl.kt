package com.davidhajas.moviebrowser.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.davidhajas.moviebrowser.datasource.movie.RoomMovieDAO
import com.davidhajas.moviebrowser.datasource.movie.entity.Movie
import com.davidhajas.moviebrowser.datasource.user.RoomUserDAO
import com.davidhajas.moviebrowser.datasource.user.entity.User

@Database(entities = [User::class, Movie::class], version = 2)
abstract class RoomDatabaseImpl : RoomDatabase() {

    abstract fun userDao(): RoomUserDAO
    abstract fun movieDao(): RoomMovieDAO

    companion object {
        private var INSTANCE: RoomDatabaseImpl? = null

        fun getInstance(context: Context): RoomDatabaseImpl {
            if (INSTANCE == null) {
                synchronized(RoomDatabaseImpl::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDatabaseImpl::class.java,
                        "database.db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE!!
        }
    }
}

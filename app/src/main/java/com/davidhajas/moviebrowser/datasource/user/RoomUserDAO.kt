package com.davidhajas.moviebrowser.datasource.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.davidhajas.moviebrowser.datasource.user.entity.User

@Dao
interface RoomUserDAO : UserDataRepository {

    // region UserDataRepository

    @Query("SELECT * from users")
    override fun getUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    override fun getUser(id: Long): User?

    @Query("SELECT * FROM users WHERE user_name LIKE :userName LIMIT 1")
    override fun getUser(userName: String): User?

    @Insert
    override fun insertUser(user: User): Long

    // endregion
}
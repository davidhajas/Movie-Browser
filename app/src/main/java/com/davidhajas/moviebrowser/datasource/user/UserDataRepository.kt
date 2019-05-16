package com.davidhajas.moviebrowser.datasource.user

import com.davidhajas.moviebrowser.datasource.user.entity.User

interface UserDataRepository {
    fun getUsers(): List<User>
    fun getUser(id : Long): User?
    fun getUser(userName: String): User?
    fun insertUser(user: User): Long
}
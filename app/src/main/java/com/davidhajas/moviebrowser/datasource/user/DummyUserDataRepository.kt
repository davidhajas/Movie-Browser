package com.davidhajas.moviebrowser.datasource.user

import com.davidhajas.moviebrowser.datasource.user.entity.User

class DummyUserDataRepository : UserDataRepository {

    // region UserDataRepository

    override fun getUsers(): List<User> {
        return listOf()
    }

    override fun getUser(id: Long): User? {
        return null
    }

    override fun getUser(userName: String): User? {
        return null
    }

    override fun insertUser(user: User): Long {
        return 0
    }

    // endregion
}
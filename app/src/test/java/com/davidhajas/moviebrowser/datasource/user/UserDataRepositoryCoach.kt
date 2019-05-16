package com.davidhajas.moviebrowser.datasource.user

import com.nhaarman.mockitokotlin2.whenever
import com.davidhajas.moviebrowser.datasource.user.entity.User

class UserDataRepositoryCoach(private val userDataRepository: UserDataRepository) {

    fun trainGetUser(id: Long, user: User?) {
        whenever(userDataRepository.getUser(id)).thenReturn(user)
    }

    fun trainGetUser(userName: String, user: User?) {
        whenever(userDataRepository.getUser(userName)).thenReturn(user)
    }
}
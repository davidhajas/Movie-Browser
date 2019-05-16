package com.davidhajas.moviebrowser.datasource.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @ColumnInfo(name = "user_name") var userName: String,
    @ColumnInfo(name = "password_hash") var passwordHash: String,
    @PrimaryKey(autoGenerate = true)  @ColumnInfo(name = "id") var id: Long = 0)
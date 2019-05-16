package com.davidhajas.moviebrowser.datasource.movie.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie(
    @ColumnInfo(name = "title") @SerializedName("Title") val title: String,
    @ColumnInfo(name = "cover_url") @SerializedName("Poster") var coverURL: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean,
    @PrimaryKey @SerializedName("imdbID") @ColumnInfo(name = "id") @NonNull val id: String
)
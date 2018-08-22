package com.fenchtose.movieratings.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Relation

data class RecentlyBrowsedMovie(
    @ColumnInfo(name = "IMDBID")
    var id: String,

    @ColumnInfo(name = "TIMESTAMP")
    var timestamp: Long,

    @Relation(parentColumn = "IMDBID", entityColumn = "IMDBID")
    var movies: List<Movie>?,

    @Ignore
    var movie: Movie?) {

    constructor(): this("", -1, null, null)
}
package com.fenchtose.movieratings.model.entity

import android.arch.persistence.room.*
import com.fenchtose.movieratings.model.db.MovieTypeConverter2
import com.fenchtose.movieratings.util.replace
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "MOVIES"/*, indices = arrayOf(Index("IMDBID", unique = true))*/)
@TypeConverters(MovieTypeConverter2::class)
@JsonClass(generateAdapter = true)
data class Movie(

    @Json(name="id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "TITLE")
    @Json(name="Title")
    var title: String = "",

    @ColumnInfo(name = "POSTER")
    @Json(name="Poster")
    var poster: String = "",

    @ColumnInfo(name = "RATINGS")
    @Json(name="Ratings")
    var ratings: List<Rating> = listOf(),

    @ColumnInfo(name = "TYPE")
    @Json(name="Type")
    var movieType: String = "",

    @ColumnInfo(name = "IMDBID")
    @Json(name="imdbID")
    var imdbId: String = "",

    @ColumnInfo(name = "YEAR")
    @Json(name="Year")
    var year: String = "",

    @ColumnInfo(name = "RATED")
    @Json(name="Rated")
    var rated: String = "",

    @ColumnInfo(name = "RELEASED")
    @Json(name="Released")
    var released: String = "",

    @ColumnInfo(name = "RUNTIME")
    @Json(name="Runtime")
    var runtime: String = "",

    @ColumnInfo(name = "GENRE")
    @Json(name="Genre")
    var genre: String = "",

    @ColumnInfo(name = "DIRECTOR")
    @Json(name="Director")
    var director: String = "",

    @ColumnInfo(name = "WRITERS")
    @Json(name="Writer")
    var writers: String = "",

    @ColumnInfo(name = "ACTORS")
    @Json(name="Actors")
    var actors: String = "",

    @ColumnInfo(name = "PLOT")
    @Json(name="Plot")
    var plot: String = "",

    @ColumnInfo(name = "LANGUAGE")
    @Json(name="Language")
    var language: String = "",

    @ColumnInfo(name = "COUNTRY")
    @Json(name="Country")
    var country: String = "",

    @ColumnInfo(name = "AWARDS")
    @Json(name="Awards")
    var awards: String = "",

    @ColumnInfo(name = "IMDBVOTES")
    @Json(name="imdbVotes")
    var imdbVotes: String = "",

    @ColumnInfo(name = "PRODUCTION")
    @Json(name="Production")
    var production: String = "",

    @ColumnInfo(name = "WEBSITE")
    @Json(name="Website")
    var website: String = "",

    @ColumnInfo(name = "TOTALSEASONS")
    @Json(name="totalSeasons")
    var totalSeasons: Int = -1,

    @Ignore // Room
    var liked: Boolean,

    @Ignore
    var appliedPreferences: AppliedPreferences) {

    @Ignore // Room
//    @Transient
    var collections: List<MovieCollection>? = null

//    @Ignore
    constructor(): this(0, "", "", listOf(), "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "",
            "", "", -1, false, AppliedPreferences())

    override fun toString(): String {
        return "Movie(id='$id', title='$title', liked='$liked')"
    }

    private fun checkValidBase(): Boolean {
        return checkValid(title, year, imdbId, movieType, poster)
    }

    private fun checkValidExtras(): Boolean {
        return checkValid(rated, released, runtime, genre, language, actors, plot, website, writers)
    }

    fun isComplete(check: Check): Boolean {
        return when(check) {
            Check.BASE -> checkValidBase()
            Check.EXTRA -> checkValidBase() && checkValidExtras()
            Check.LIKED -> checkValidBase() && checkValidExtras() && appliedPreferences.liked
            Check.USER_PREFERENCES -> checkValidBase() && checkValidExtras() && appliedPreferences.checkValid()
        }
    }

    private fun checkValid(vararg fields: String?): Boolean {
        return fields.filter {
            !it.isNullOrEmpty()
        }.size > fields.size/2
    }

    companion object {
        fun empty() : Movie {
            throw RuntimeException("fucked")
//            return Movie(id = -1, imdbId = "", title="", type="")
        }
    }

}

enum class Check {
    BASE,
    EXTRA,
    LIKED,
    USER_PREFERENCES
}

data class AppliedPreferences(val liked: Boolean = false, val collections: Boolean = false) {
    fun checkValid(): Boolean {
        return liked && collections
    }
}

class Rating(@SerializedName("Source") val source: String, @SerializedName("Value") val value: String) {
    override fun toString(): String {
        return "Rating(source='$source', value='$value')"
    }

    companion object {
        fun empty(): Rating {
            return Rating("", "")
        }
    }
}

fun List<Movie>.hasMovie(movie: Movie): Int {
    forEachIndexed {
        index, m -> if (m.imdbId == movie.imdbId) { return index }
    }

    return -1
}

fun List<Movie>.updateMovie(movie: Movie): List<Movie> {
    val index = hasMovie(movie)
    if (index != -1) {
        return this.replace(index, movie)
    }

    return this
}
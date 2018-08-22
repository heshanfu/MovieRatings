package com.fenchtose.movieratings.util

import com.fenchtose.movieratings.model.entity.Movie

fun<T> List<T>.addAll(collection: Collection<T>): List<T> {
    val data = ArrayList<T>(this)
    data.addAll(collection)
    return data
}

fun <T> List<T>.replace(index: Int, t: T): List<T> {
    if (index < 0 || index >= size) {
        throw IndexOutOfBoundsException("Invalid index: $index provided. list size is $size")
    }

    val data = ArrayList(this)
    data.removeAt(index)
    data.add(index, t)

    return data
}

fun List<Movie>.update(movie: Movie): List<Movie> {
    var index = -1
    forEachIndexed {
        ind, mov -> if (mov.imdbId == movie.imdbId) {
            index = ind
            return@forEachIndexed
        }
    }

    if (index != -1) {
        return toMutableList().apply {
            removeAt(index)
            add(index, movie)
        }
    }

    return this
}

fun <T> List<T>.removeAt(index: Int): List<T> {
    if (index in 0..(size - 1)) {
        return toMutableList().apply { removeAt(index) }
    }

    return this
}

fun <T> List<T>.add(index: Int, t: T, adjust: Boolean = true): List<T> {
    if (index in 0..(size - 1)) {
        return toMutableList().apply { add(index, t) }
    }

    if (adjust) {
        return toMutableList().apply { add(t) }
    }

    throw IndexOutOfBoundsException("Invalid index $index for list of size $size")
}
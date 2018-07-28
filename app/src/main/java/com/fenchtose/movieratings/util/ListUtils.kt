package com.fenchtose.movieratings.util

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
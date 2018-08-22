package com.fenchtose.movieratings.model.db

import android.support.annotation.WorkerThread
import com.fenchtose.movieratings.model.entity.Movie

interface UserPreferenceApplier {
    @WorkerThread
    fun applyPreference(movie: Movie): Movie
}

fun Collection<UserPreferenceApplier>.applyPreference(movie: Movie): Movie {
    var updated = movie
    forEach { updated = it.applyPreference(updated) }
    return updated
}
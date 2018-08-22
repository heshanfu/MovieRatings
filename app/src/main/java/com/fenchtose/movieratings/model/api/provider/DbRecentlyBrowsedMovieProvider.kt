package com.fenchtose.movieratings.model.api.provider

import com.fenchtose.movieratings.model.entity.RecentlyBrowsedMovie
import com.fenchtose.movieratings.model.db.UserPreferenceApplier
import com.fenchtose.movieratings.model.db.applyPreference
import com.fenchtose.movieratings.model.db.dao.MovieDao
import com.fenchtose.movieratings.util.replace
import io.reactivex.Observable

class DbRecentlyBrowsedMovieProvider(private val movieDao: MovieDao): RecentlyBrowsedMovieProvider {

    private val preferenceAppliers = ArrayList<UserPreferenceApplier>()

    override fun getMovies(): Observable<List<RecentlyBrowsedMovie>> {
        return Observable.fromCallable {
            movieDao.getRecentlyBrowsedMovies()
                    .filter {
                        it.movies != null && it.movies!!.isNotEmpty()
                    }
            }.map {
                it.map {
                    val movies = it.movies
                    if (movies != null && movies.isNotEmpty()) {
                        it.copy(movies = movies.replace(0, preferenceAppliers.applyPreference(movies[0])))
                    } else {
                        it
                    }
                }
            }
    }

    override fun addPreferenceApplier(applier: UserPreferenceApplier) {
        preferenceAppliers.add(applier)
    }
}
package com.fenchtose.movieratings.features.moviecollection.collectionpage

import android.net.Uri
import com.fenchtose.movieratings.base.redux.Dispatch
import com.fenchtose.movieratings.features.baselistpage.BaseMovieListPage
import com.fenchtose.movieratings.model.entity.Movie

interface CollectionPage: BaseMovieListPage {

    fun updateState(state: OpState)
    fun updateState(state: ShareState)

    fun getDispatcher(): Dispatch?

    sealed class OpState(val movie: Movie) {
        class Removed(val movies: List<Movie>, movie: Movie, val position: Int): OpState(movie)
        class Added(val movies: List<Movie>, movie: Movie, val position: Int): OpState(movie)
        class RemoveError(movie: Movie): OpState(movie)
        class AddError(movie: Movie): OpState(movie)
    }

    sealed class ShareState {
        class Started: ShareState()
        class Error: ShareState()
        class Success(val uri: Uri): ShareState()
    }
}
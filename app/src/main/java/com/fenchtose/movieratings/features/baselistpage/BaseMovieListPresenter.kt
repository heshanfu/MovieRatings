package com.fenchtose.movieratings.features.baselistpage

import android.support.annotation.CallSuper
import android.support.annotation.VisibleForTesting
import android.view.View
import com.fenchtose.movieratings.base.Presenter
import com.fenchtose.movieratings.base.router.Router
import com.fenchtose.movieratings.features.moviepage.MoviePageFragment
import com.fenchtose.movieratings.model.entity.Movie
import com.fenchtose.movieratings.model.db.like.LikeStore
import com.fenchtose.movieratings.util.RxHooks
import com.fenchtose.movieratings.util.update
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

abstract class BaseMovieListPresenter<V :BaseMovieListPage>(
        protected val rxHooks: RxHooks,
        private val likeStore: LikeStore,
        protected val router: Router?): Presenter<V>() {

    protected var data: List<Movie>? = null

    @CallSuper
    override fun attachView(view: V) {
        super.attachView(view)
        loadData()
    }

    open protected fun loadData() {
        val d = load()
                .subscribeOn(rxHooks.ioThread())
                .observeOn(rxHooks.mainThread())
                .subscribeBy(
                        onNext = {
                            updateData(ArrayList(it))
                        },
                        onError = {
                            data = null
                            it.printStackTrace()
                            getView()?.updateState(BaseMovieListPage.State.Error())
                        }
                )

        subscribe(d)
    }

    open protected fun updateData(movies: List<Movie>) {
        this.data = movies

        val state = if (movies.isEmpty()) {
            BaseMovieListPage.State.Empty()
        } else {
            BaseMovieListPage.State.Success(movies)
        }

        getView()?.updateState(state)
    }

    open fun toggleLike(movie: Movie) {
        likeStore.setLiked(movie.imdbId, !movie.liked)
        data?.let {
            updateData(it.update(movie.copy(liked = !movie.liked)))
        }
    }

    fun openMovie(movie: Movie, sharedElement: Pair<View, String>?) {
        router?.go(MoviePageFragment.MoviePath(movie, sharedElement))
    }

    abstract fun load(): Observable<List<Movie>>

    @VisibleForTesting
    fun getDataForTest(): List<Movie>? = data
}
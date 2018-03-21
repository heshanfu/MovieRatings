package com.fenchtose.movieratings.features.moviecollection.collectionlist

import com.fenchtose.movieratings.base.Presenter
import com.fenchtose.movieratings.model.api.provider.MovieCollectionProvider
import com.fenchtose.movieratings.model.db.movieCollection.MovieCollectionStore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CollectionListPresenter(
        private val provider: MovieCollectionProvider,
        private val collectionStore: MovieCollectionStore) : Presenter<CollectionListPage>() {

    override fun attachView(view: CollectionListPage) {
        super.attachView(view)
        loadCollections()
    }

    private fun loadCollections() {
        provider.getCollections()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isEmpty()) {
                        updateState(CollectionListPage.State(CollectionListPage.Ui.EMPTY))
                    } else {
                        updateState(CollectionListPage.State(CollectionListPage.Ui.DATA_LOADED, ArrayList(it)))
                    }
                }, {
                    it.printStackTrace()
                    updateState(CollectionListPage.State(CollectionListPage.Ui.ERROR))
                })
    }

    private fun updateState(state: CollectionListPage.State) {
        getView()?.updateState(state)
    }

    fun createCollection(name: String) {
        collectionStore.createCollection(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateState(CollectionListPage.State(CollectionListPage.Ui.COLLECTION_CREATED))
                    loadCollections()
                }, {
                    it.printStackTrace()
                    updateState(CollectionListPage.State(CollectionListPage.Ui.COLLECTION_CREATE_ERROR))
                })
    }
}
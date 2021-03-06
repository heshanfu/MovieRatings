package com.fenchtose.movieratings.base.router

import android.os.Build
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import com.fenchtose.movieratings.base.BaseFragment
import com.fenchtose.movieratings.base.RouterPath
import com.fenchtose.movieratings.R
import com.fenchtose.movieratings.base.RouterBaseActivity
import com.fenchtose.movieratings.features.moviepage.DetailTransition
import com.fenchtose.movieratings.features.moviepage.MoviePageFragment
import com.fenchtose.movieratings.features.premium.DonatePageFragment
import java.util.Stack

class Router(activity: RouterBaseActivity,
             private val onMovedTo: (RouterPath<out BaseFragment>) -> Unit,
             private val onRemoved: (RouterPath<out BaseFragment>) -> Unit) {

    private val history: Stack<RouterPath<out BaseFragment>> = Stack()
    private val manager = activity.supportFragmentManager
    private val titlebar: ActionBar? = activity.supportActionBar

    private val keyPathMap: HashMap<String, ((Bundle) -> RouterPath<out BaseFragment>)> = HashMap()

    private val TAG = "Router"

    companion object {
        val ROUTE_TO_SCREEN = "route_to_screen"
        val HISTORY = "history"
    }

    init {
        keyPathMap.put(DonatePageFragment.DonatePath.KEY, DonatePageFragment.DonatePath.createPath())
        keyPathMap.put(MoviePageFragment.MoviePath.KEY, MoviePageFragment.MoviePath.createPath())
    }

    fun canHandleKey(key: String): Boolean {
        return keyPathMap.containsKey(key)
    }

    fun buildRoute(path: RouterPath<out BaseFragment>): Router {
        history.push(path)
        return this
    }

    fun buildRoute(extras: Bundle): Router {
        keyPathMap[extras.getString(ROUTE_TO_SCREEN, "")]?.invoke(extras)?.let { buildRoute(it) }
        return this
    }

    fun start() {
        if (history.isNotEmpty()) {
            move(history.peek())
        }
    }

    fun go(path: RouterPath<out BaseFragment>) {
        if (history.size >= 1) {
            val top = history.peek()
            if (top.javaClass == path.javaClass) {
                return
            }

            top.saveState()
        }

        move(path)
        history.push(path)
    }

    fun onBackRequested(): Boolean {
        if (history.empty()) {
            Log.e(TAG, "history is empty. We can't go back")
            return true
        }

        val canTopGoBack = canTopGoBack()
        if (canTopGoBack) {
            if (history.size == 1) {
                return  true
            }

            goBack()
        }

        return false
    }

    private fun goBack(): Boolean {

        if (history.size > 1) {
            moveBack()
            return true
        }

        return false
    }

    private fun canTopGoBack(): Boolean {
        val fragment: BaseFragment? = getTopView()

        fragment?.let {
            return fragment.canGoBack()
        }

        return true

    }

    private fun getTopView() : BaseFragment? {
        return history.peek().fragment
    }

    private fun move(path: RouterPath<out BaseFragment>) {
        path.attachRouter(this)
        val fragment = path.createOrGetFragment()
        val transaction = manager.beginTransaction().replace(R.id.fragment_container, fragment)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            path.getSharedTransitionElement()?.let {
            transaction.addSharedElement(it.first, it.second)
                fragment.sharedElementEnterTransition = DetailTransition()
                fragment.sharedElementReturnTransition = DetailTransition()
            }
        }

        transaction.commit()
        titlebar?.let {
            it.setTitle(fragment.getScreenTitle())
            it.setDisplayShowHomeEnabled(path.showBackButton())
            it.setDisplayHomeAsUpEnabled(path.showBackButton())
        }

        onMovedTo.invoke(path)
    }

    fun updateTitle(title: CharSequence) {
        titlebar?.title = title
    }

    private fun moveBack() {
        val path = history.pop()
        path.clearState()
        path.detach()
        onRemoved.invoke(path)
        if (!history.empty()) {
            val top = history.peek()
            top?.let {
                move(top)
            }
        }
    }

    class History {

        val history = ArrayList<Pair<String, Bundle>>()

        constructor()
        constructor(extras: Bundle) {
            if (!extras.containsKey(KEY_PATHS)) {
                return
            }

            val pathKeys = extras.getStringArrayList(KEY_PATHS) ?: return
            pathKeys.forEach {
                if (extras.containsKey(it)) {
                    history.add(Pair(it, extras.getBundle(it)))
                }
            }
        }

        companion object {
            private val KEY_PATHS = "paths"
        }

        fun addPath(pathKey: String, extras: Bundle): History {
            history.add(Pair(pathKey, extras))
            return this
        }

        fun toBundle(): Bundle {
            val extras = Bundle()
            extras.putStringArrayList(KEY_PATHS, ArrayList(history.map { it.first }))
            history.forEach {
                extras.putString(ROUTE_TO_SCREEN, it.first)
                extras.putBundle(it.first, it.second)
            }

            return extras
        }

        fun isEmpty(): Boolean = history.isEmpty()
    }
}
package com.fenchtose.movieratings.features.settings

import android.os.Bundle
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fenchtose.movieratings.MovieRatingsApplication
import com.fenchtose.movieratings.R
import com.fenchtose.movieratings.base.BaseFragment
import com.fenchtose.movieratings.base.RouterPath

class SettingsFragment2: BaseFragment() {
    override fun canGoBack() = true
    override fun getScreenTitle() = R.string.settings_header

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_page_redesign_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindAction(view, R.id.settings_app_section, AppSectionFragment.SettingsAppSectionPath())
    }

    private fun bindAction(root: View, @IdRes id: Int, path: RouterPath<out BaseFragment>) {
        root.findViewById<View?>(id)?.setOnClickListener {
            MovieRatingsApplication.router?.go(path)
        }
    }

    class SettingsPath: RouterPath<SettingsFragment2>() {
        override fun createFragmentInstance(): SettingsFragment2 {
            return SettingsFragment2()
        }
    }
}
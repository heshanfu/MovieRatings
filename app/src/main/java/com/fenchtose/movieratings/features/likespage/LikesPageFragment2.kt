package com.fenchtose.movieratings.features.likespage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fenchtose.movieratings.R
import com.fenchtose.movieratings.base.BaseFragment
import com.fenchtose.movieratings.base.RouterPath

class LikesPageFragment2: BaseFragment() {
    override fun getScreenTitle() = R.string.likes_page_title
    override fun canGoBack() = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.base_movies_list_page_layout, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        render({
            appState, dispatch ->
        })
    }
}

class LikesPath: RouterPath<LikesPageFragment2>() {
    override fun createFragmentInstance(): LikesPageFragment2 {
        return LikesPageFragment2()
    }
}
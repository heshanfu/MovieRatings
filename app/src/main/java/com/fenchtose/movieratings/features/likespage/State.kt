package com.fenchtose.movieratings.features.likespage

import com.fenchtose.movieratings.model.entity.Movie

data class LikedPageState(
        val movies: List<Movie> = listOf()
)
package com.fenchtose.movieratings.model.preferences

import android.support.annotation.ColorInt
import com.fenchtose.movieratings.model.entity.Sort

interface UserPreferences {

    companion object {
//        val NETFLIX = "netflix"
//        val PRIMEVIDEO = "primevideo"
//        val PLAY_MOVIES = "play_movies"
//        val HOTSTAR = "hotstar"
//        val YOUTUBE = "youtube"
//        val BBC_IPLAYER = "bbc_iplayer"
        val SAVE_HISTORY = "save_history"
        val USE_TTS = "use_tts"
        val TTS_AVAILABLE = "tts_available"
        val SHOW_ACTIVATE_FLUTTER = "show_activate_flutter"
        val USE_YEAR = "use_year"
        val ONBOARDING_SHOWN = "onboarding_shown"
        val LOCALE_INFO_SHOWN = "locale_info_shown"

        val SHOW_SUPPORT_APP_PROMPT = "show_support_app"
        val SHOW_RATE_APP_PROMPT = "show_rate_app"

        val USE_FLUTTER_API = "use_flutter_api"

        val OPEN_MOVIE_IN_APP = "open_movie_in_app"
    }

    /**
     * returns if particular app is enabled or not. Default is true.
     */
    fun isAppEnabled(app: String): Boolean

    /**
     * returns if particular setting is enabled or not. Default is false.
     */
    fun isSettingEnabled(app: String): Boolean

    fun setEnabled(app: String, status: Boolean)

    fun getRatingDisplayDuration():Int
    fun setRatingDisplayDuration(durationInMS: Int)

    fun setLatestLikeSort(type: Sort)
    fun getLatestLikeSort(): Sort

    fun setLatestCollectionSort(collectionId: Long?, type: Sort)
    fun getLatestCollectionSort(collectionId: Long?): Sort

    fun getBubbleColor(@ColorInt fallback: Int): Int
    fun setBubbleColor(@ColorInt color: Int)

    fun getBubblePosition(fallbackY: Int): Pair<Int, Boolean>
    fun setBubblePosition(y: Int, left: Boolean)
}
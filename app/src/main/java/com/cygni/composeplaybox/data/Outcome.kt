package com.cygni.composeplaybox.data

/**
 * A sealed class containing Success / Error states.
 * Could also contain Loading states, but I'm less inclined to add those, as they are more likely to be specific to different situations.
 */
sealed class Outcome<out T : Any> {
    data class Success<out T : Any>(val result: T) : Outcome<T>()
    data class Error(val message: String, val error: Throwable? = null) : Outcome<Nothing>()
}
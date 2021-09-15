package com.app.demo.utils

class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
/**
 * Execute @param block if value was not handled before
 */
fun <T> Event<T>?.handleValue(block: (T) -> Unit) {
    this?.getContentIfNotHandled()?.let { value ->
        block(value)
    }
}
/**
 * Execute @param block if value was not handled before
 */
fun <T> Event<T>?.handle(block: () -> Unit) {
    this?.getContentIfNotHandled()?.let { value ->
        block()
    }
}

fun <T : Any> T.event() = Event(this)
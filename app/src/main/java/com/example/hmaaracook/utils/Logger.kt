package com.example.hmaaracook.utils

import android.util.Log

object Logger {
    private const val PREFIX = "HmaaraCook"

    fun d(tag: String, message: String) {
        Log.d("$PREFIX-$tag", message)
    }

    fun i(tag: String, message: String) {
        Log.i("$PREFIX-$tag", message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("$PREFIX-$tag", message, throwable)
    }

    fun w(tag: String, message: String) {
        Log.w("$PREFIX-$tag", message)
    }

    fun v(tag: String, message: String) {
        Log.v("$PREFIX-$tag", message)
    }
}

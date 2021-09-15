package com.app.demo.utils

import android.content.res.Resources

// Px to Dp
@Suppress("unused")
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

// Dp to Px
@Suppress("unused")
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
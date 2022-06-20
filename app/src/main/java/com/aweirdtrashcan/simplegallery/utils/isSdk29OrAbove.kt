package com.aweirdtrashcan.simplegallery.utils

import android.os.Build

inline fun <T> isSdk29OrAbove(onSdk29 : () -> T) : T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}
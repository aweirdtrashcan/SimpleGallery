package com.aweirdtrashcan.simplegallery.models

import android.net.Uri

data class Photos(
    val id : Long,
    val displayName : String,
    val width : Long,
    val height : Long,
    val uri : Uri
)
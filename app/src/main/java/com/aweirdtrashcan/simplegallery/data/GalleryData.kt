package com.aweirdtrashcan.simplegallery.data

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import com.aweirdtrashcan.simplegallery.models.Photos
import com.aweirdtrashcan.simplegallery.utils.isSdk29OrAbove
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class GalleryData @Inject constructor(private val application: Application) {

    suspend fun getPhotos() : List<Photos> {
        return withContext(Dispatchers.IO) {
            val contentResolver = application.contentResolver

            val queries = isSdk29OrAbove {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val metadatas = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT
            )

            val photosArray = mutableListOf<Photos>()
            contentResolver.query(
                queries,
                metadatas,
                null,
                null,
                null
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

                while(cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getLong(widthColumn)
                    val height = cursor.getLong(heightColumn)

                    val contentUris = ContentUris.withAppendedId(
                        queries,
                        id
                    )
                    photosArray.add(Photos(id, displayName, width, height, contentUris))
                }
                photosArray.toMutableList()
            } ?: listOf()
        }
    }
}
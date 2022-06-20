package com.aweirdtrashcan.simplegallery.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.aweirdtrashcan.simplegallery.data.GalleryData
import com.aweirdtrashcan.simplegallery.models.Photos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val galleryData: GalleryData,
) : ViewModel() {

    suspend fun getGallery() : List<Photos> {
        return withContext(Dispatchers.IO) {
            galleryData.getPhotos()
        }
    }

}
package com.aweirdtrashcan.simplegallery.di

import android.app.Application
import com.aweirdtrashcan.simplegallery.data.GalleryData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGalleryData(application: Application) : GalleryData {
        return GalleryData(application)
    }
}
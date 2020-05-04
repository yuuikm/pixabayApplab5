package com.lab5.pixabayapp.di

import com.lab5.pixabayapp.ui.photo.PhotoFragment
import com.lab5.pixabayapp.ui.search.SearchPhotoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributePhotoFragment(): PhotoFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchPhotoFragment(): SearchPhotoFragment
}
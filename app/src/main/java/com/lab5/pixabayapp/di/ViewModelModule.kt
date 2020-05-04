package com.lab5.pixabayapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lab5.pixabayapp.ui.photo.PhotoViewModel
import com.lab5.pixabayapp.ui.search.SearchPhotoViewModel
import com.lab5.pixabayapp.viewmodels.AppViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchPhotoViewModel::class)
    abstract fun bindSearchPhotoViewModel(searchPhotoViewModel: SearchPhotoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PhotoViewModel::class)
    abstract fun bindPhotoViewModel(photoViewModel: PhotoViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}
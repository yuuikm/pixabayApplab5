package com.lab5.pixabayapp.repository

import androidx.lifecycle.LiveData
import com.lab5.pixabayapp.database.PhotoDoa
import com.lab5.pixabayapp.models.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(
    private val photoDao: PhotoDoa
) {
    fun loadPhotoById(photoId: Int): LiveData<Photo> {
        return photoDao.getPhotoById(photoId)
    }
}
package com.lab5.pixabayapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lab5.pixabayapp.models.Photo
import com.lab5.pixabayapp.models.PhotoSearchResult


@Database(
    entities = [
        Photo::class,
        PhotoSearchResult::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PixBayDatabase : RoomDatabase() {
    abstract fun photoDao() : PhotoDoa
}
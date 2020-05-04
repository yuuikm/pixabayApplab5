package com.lab5.pixabayapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.lab5.pixabayapp.AppExecutors
import com.lab5.pixabayapp.database.PixBayDatabase
import com.lab5.pixabayapp.database.PhotoDoa
import com.lab5.pixabayapp.models.Photo
import com.lab5.pixabayapp.models.PhotoSearchResult
import com.lab5.pixabayapp.models.Resource
import com.lab5.pixabayapp.api.ApiResponse
import com.lab5.pixabayapp.api.PixBayService
import com.lab5.pixabayapp.api.PhotoSearchResponse
import com.lab5.pixabayapp.utils.AbsentLiveData
import com.lab5.pixabayapp.utils.Constants
import com.lab5.pixabayapp.utils.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: PixBayDatabase,
    private val photoDao: PhotoDoa,
    private val pixBayService: PixBayService
) {

    private val photoListRateLimit = RateLimiter<String>(60, TimeUnit.MINUTES)


    fun search(query: String, pageNumber: Int): LiveData<Resource<List<Photo>>> {
        return object : NetworkBoundResource<List<Photo>, PhotoSearchResponse>(appExecutors) {
            override fun saveCallResult(item: PhotoSearchResponse) {

                val ids = arrayListOf<Int>()
                val photoIds: List<Int> = item.photos.map { it.id }


                if (pageNumber != 1 ) {
                    val prevPageNumber = pageNumber - 1
                    val photoSearchResult = photoDao.searchResult(query, prevPageNumber)
                    ids.addAll(photoSearchResult.photoIds)
                }

                ids.addAll(photoIds)
                val photoResult = PhotoSearchResult(
                    query = query,
                    photoIds = ids,
                    pageNumber = pageNumber
                )

                db.runInTransaction {
                    photoDao.insertPhotos(item.photos)
                    photoDao.insert(photoResult)
                }

            }

            override fun shouldFetch(data: List<Photo>?): Boolean {
                return data == null || data.isEmpty() || photoListRateLimit.shouldFetch(query)
            }

            override fun loadFromDb(): LiveData<List<Photo>> { // at the Very beginning When pageNumber = 1 --->(query, 2) -> null
                return Transformations.switchMap(photoDao.search(query, pageNumber)) { searchData ->

                    if (searchData == null ) {
                        AbsentLiveData.create()
                    } else {
                        photoDao.loadOrdered(searchData.photoIds)
                    }
                }
            }

            override fun onFetchFailed() {
                photoListRateLimit.reset(query)
            }

            override fun createCall(): LiveData<ApiResponse<PhotoSearchResponse>> {
                return pixBayService.searchPhotos(Constants.API_KEY, query, pageNumber)
            }

        }.asLiveData()
    }
}
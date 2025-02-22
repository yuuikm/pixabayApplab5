package com.lab5.pixabayapp.ui.search

import androidx.lifecycle.*
import com.lab5.pixabayapp.models.Photo
import com.lab5.pixabayapp.models.Resource
import com.lab5.pixabayapp.utils.Status
import com.lab5.pixabayapp.repository.PhotosRepository
import com.lab5.pixabayapp.utils.AbsentLiveData
import java.util.*
import javax.inject.Inject

class SearchPhotoViewModel @Inject constructor(private val photoRepository: PhotosRepository) :
    ViewModel() {

    private val _query = MutableLiveData<String>()

    val query = _query
    var isPerformingNextQuery = false

    private var isPerformingQuery = false
    private var pageNumber: Int = 1
    private val loadMoreState = MutableLiveData<LoadMoreState>()
    private var results = MutableLiveData<Resource<List<Photo>>>()

    val photos = MediatorLiveData<Resource<List<Photo>>>()

    var isScreenRotated = true

    /**
     * Set the query, it will trigger results MutableLiveData as the _query is the source of it.
     * @param originalInput the query
     */
    private fun setQuery(originalInput: String?) {
        isPerformingQuery = true
        val input = originalInput?.toLowerCase(Locale.getDefault())?.trim()
        if (input == _query.value) {
            return
        }
        _query.value = input
    }

    fun refresh() {
        _query.value?.let {
            _query.value = it
        }
        executeSearch(_query.value, 1)
    }

    /**
     * Execute the search process
     *
     * @param query the query to be searched
     * @param pageNumber the pageNumber it should be in the ViewModel
     */
    fun executeSearch(query: String?, pageNumber: Int) {
        this.pageNumber = pageNumber

        //set Query will trigger Result LiveData as _query has Changed then I am adding Result to MediatorLiveData
        // Cuz i wanna analyze the data before sending and observing form the UI (SearchFragment in my case)
        setQuery(query)
        isPerformingQuery = true
        //Transformations.switchMap(source, function) observes the Value source LiveData and returns LiveData.
        //Transformations.map(source, function) observes the Value source LiveData and returns value.
        results = Transformations.switchMap(_query) { search ->
            if (search.isNullOrBlank()) {
                AbsentLiveData.create()
            } else {
                photoRepository.search(search, pageNumber)
            }
        } as MutableLiveData<Resource<List<Photo>>>

        photos.addSource(results) {
            if (it != null) {
                photos.value = it
                if (it.status == Status.SUCCESS) {
                    if (isPerformingNextQuery) {
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = null
                            )
                        )
                    }
                    isPerformingQuery = false
                    photos.removeSource(results)

                } else if (it.status == Status.ERROR) {
                    isPerformingQuery = false
                    photos.removeSource(results)
                    if (isPerformingNextQuery) {
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = it.message
                            )
                        )
                    }
                }

            } else {
                photos.removeSource(results)
            }
        }
    }

    /**
     * Search the next page
     *
     */
    fun searchNextPage() {

        if (!isPerformingQuery) {
            isPerformingNextQuery = true

            loadMoreState.value = LoadMoreState(
                isRunning = true,
                errorMessage = null
            )
            pageNumber += 1
            executeSearch(_query.value, pageNumber)
        }
    }

    fun getPhotos(): LiveData<Resource<List<Photo>>> {
        return photos
    }


    val loadMoreStatus: LiveData<LoadMoreState>
        get() = loadMoreState


    /**
     * Hekper class for loading new pages
     * @property isRunning
     * @property errorMessage
     */
    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false
        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }
}
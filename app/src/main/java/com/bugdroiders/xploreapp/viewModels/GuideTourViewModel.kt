package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.data.api.StoreAPI
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.models.Tour
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GuideTourViewModel(app: Application) : AndroidViewModel(app) {
    private val api = FirestoreAPI()

    val isTourCreated : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData()

    var tourImageUri : MutableLiveData<Uri> = MutableLiveData()

    val storageAPI = StoreAPI()
    private fun showLoading() {
        isLoading.postValue(true)
    }

    private fun dismissLoading() {
        isLoading.postValue(false)
    }

    fun createTour(tour: Tour) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            tourImageUri.value?.let {
                storageAPI.uploadImage(it) {downloadUrl ->
                    if (downloadUrl != null) {
                        val newTour = Tour(
                            image = downloadUrl,
                            title = tour.title,
                            description = tour.description,
                            tourLocation = tour.tourLocation,
                            price = tour.price,
                            guide = tour.guide,
                            guideId = tour.guideId,
                            guideImage = tour.guideImage,
                            tourDuration = tour.tourDuration,
                            tourType = tour.tourType,
                            schedule = tour.schedule
                        )
                        val db = Firebase.firestore
                        db.collection("tours").add(newTour)
                            .addOnCompleteListener {task ->
                                if (task.isSuccessful) {
                                    isTourCreated.postValue(true)
                                    dismissLoading()
                                    isTourCreated.postValue(true)
                                }
                                else {
                                    isTourCreated.postValue(false)
                                }
                            }
                    }
                }
            }
        }
    }
}
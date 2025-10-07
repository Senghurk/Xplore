package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.models.Review
import com.bugdroiders.xploreapp.models.Tour
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuideTourDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val api = FirestoreAPI()

    val isDeleted : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val tour: MutableLiveData<Tour?> = MutableLiveData()
    val reviews : MutableLiveData<List<Review>> = MutableLiveData()

    fun deleteTour() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            isDeleted.postValue(api.deleteTour(tour.value?.id.toString()))
            dismissLoading()
        }
    }

    fun getTourDetail(tourId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            val tourInfo = api.getTour(tourId)
            if (tourInfo != null) {
                tour.postValue(tourInfo)
                Log.d("TourDetail", tour.value?.guideId.toString())
                dismissLoading()
            }
        }
    }

    fun getTourReviews(tourId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            reviews.postValue(api.getTourReviews(tourId))
            dismissLoading()
        }
    }


    private fun showLoading() {
        isLoading.postValue(true)
    }

    private fun dismissLoading() {
        isLoading.postValue(false)
    }
}
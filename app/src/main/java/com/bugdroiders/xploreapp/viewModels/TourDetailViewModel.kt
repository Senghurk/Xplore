package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.models.Booking
import com.bugdroiders.xploreapp.models.Review
import com.bugdroiders.xploreapp.models.Tour
import com.bugdroiders.xploreapp.models.User
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class TourDetailViewModel(app: Application): AndroidViewModel(app) {
    private val api = FirestoreAPI()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData()

    val tourInfo : MutableLiveData<Tour?> = MutableLiveData()
    val reviews : MutableLiveData<List<Review>> = MutableLiveData()
    val isBooked : MutableLiveData<Boolean> = MutableLiveData()
    fun getTourDetail(tourId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            val tour = api.getTour(tourId)
            if (tour != null) {
                tourInfo.postValue(tour)
            }
            dismissLoading()
        }
    }

    fun getTourReviews(tourId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            reviews.postValue(api.getTourReviews(tourId))
            dismissLoading()
        }
    }

    fun bookTour(userInfo: User) {
        showLoading()
        val booking = Booking(
            travelerId = userInfo.email,
            travelerName = userInfo.fullName,
            guideId = tourInfo.value?.guideId,
            guideName = tourInfo.value?.guide,
            bookingDate = Date(),
            tourId = tourInfo.value?.id,
            tourName = tourInfo.value?.title
        )
        viewModelScope.launch(Dispatchers.IO) {
            isBooked.postValue(api.bookTour(booking))
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
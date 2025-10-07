package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.models.Booking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingViewModel(app:Application) : AndroidViewModel(app) {
    private val api = FirestoreAPI()
    val bookings : MutableLiveData<List<Booking>> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private fun showLoading() {
        isLoading.postValue(true)
    }

    private fun dismissLoading() {
        isLoading.postValue(false)
    }

    fun getBookings(userEmail: String, isTraveler: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isTraveler) {
                bookings.postValue(api.getBookingsTraveler(userEmail))
            } else {
                bookings.postValue(api.getGuideBookings(userEmail))
            }
        }
    }
}
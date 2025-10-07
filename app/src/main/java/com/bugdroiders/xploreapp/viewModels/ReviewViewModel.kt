package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.models.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewViewModel(app: Application) : AndroidViewModel(app) {
    private val api = FirestoreAPI()
    val isReviewPosted : MutableLiveData<Boolean> = MutableLiveData()

    fun createReview(review: Review, bookingId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isCreated = api.createReview(review, bookingId)
            isReviewPosted.postValue(isCreated)
        }
    }
}
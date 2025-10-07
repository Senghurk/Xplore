package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.models.Guide
import com.bugdroiders.xploreapp.models.Review
import com.bugdroiders.xploreapp.models.Tour
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TravelerGuideDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val api = FirestoreAPI()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val guideInfo: MutableLiveData<Guide> = MutableLiveData()
    val tours: MutableLiveData<List<Tour>> = MutableLiveData()
    val reviews: MutableLiveData<List<Review>> = MutableLiveData()

    private fun showLoading() {
        isLoading.postValue(true)
    }

    private fun dismissLoading() {
        isLoading.postValue(false)
    }

    fun getToursOffered(guideEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tours.postValue(api.getTours(guideEmail))
        }
    }

    fun getGuideProfile(guideEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            guideInfo.postValue(api.getGuide(guideEmail)?.toObject<Guide>() ?: Guide())
        }
    }

    fun initializePage(guideId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            guideInfo.postValue(api.getGuide(guideId)?.toObject<Guide>() ?: Guide())
            tours.postValue(api.getTours(guideId))
            reviews.postValue(api.getGuideReview(guideId))
            dismissLoading()
        }
    }
}